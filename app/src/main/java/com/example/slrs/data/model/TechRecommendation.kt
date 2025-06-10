package com.example.slrs.data.model

data class TechRecommendation(
    val name: String,
    val purpose: String,
    val roadmap: List<String>,
    val resources: List<String>,
    val roadmapLink: String? = null // ✅ optional link
)
