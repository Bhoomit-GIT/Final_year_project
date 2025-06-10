package com.example.slrs.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SmartLearningViewModel : ViewModel() {

    fun saveRoadmapRecommendation(
        userEmail: String,
        techName: String,
        roadmapUrl: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val firestore = Firebase.firestore
        val userDocRef = firestore.collection("users").document(userEmail)

        val newRoadmap = mapOf("tech" to techName, "url" to roadmapUrl)

        userDocRef.get().addOnSuccessListener { doc ->
            val currentList = (doc.get("savedRoadmaps") as? List<*>)?.mapNotNull { item ->
                (item as? Map<*, *>)?.mapNotNull { (k, v) ->
                    if (k is String && v is String) k to v else null
                }?.toMap()
            } ?: emptyList()

            // Avoid duplicates
            if (currentList.any { it["tech"] == techName && it["url"] == roadmapUrl }) {
                Log.d("SaveRoadmap", "Already saved.")
                onSuccess() // Still show toast to user
                return@addOnSuccessListener
            }

            val updatedList = currentList + newRoadmap

            userDocRef.set(mapOf("savedRoadmaps" to updatedList), SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("SaveRoadmap", "Saved successfully.")
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    Log.e("SaveRoadmap", "Failed to save", exception)
                    onFailure(exception)
                }

        }.addOnFailureListener { exception ->
            Log.e("SaveRoadmap", "Error fetching user document", exception)
            onFailure(exception)
        }
    }
}
