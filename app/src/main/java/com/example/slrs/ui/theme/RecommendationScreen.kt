package com.example.slrs.ui.theme

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.slrs.data.model.TechRecommendation
import com.example.slrs.viewmodel.SmartLearningViewModel

import androidx.compose.ui.platform.LocalContext

@Composable
fun RecommendationScreen(
    recommendations: List<TechRecommendation>,
    isLoading: Boolean,
    onRestart: () -> Unit,
    viewModel: SmartLearningViewModel,
    userEmail: String
) {
    val expandedCardIndex = remember { mutableIntStateOf(-1) }
    var viewRoadmapUrl by remember { mutableStateOf<String?>(null) }

    if (viewRoadmapUrl != null) {
        BackHandler { viewRoadmapUrl = null }

        RoadmapWebViewScreen(
            url = viewRoadmapUrl!!,
            onBack = { viewRoadmapUrl = null }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "🎯 Your Top Tech Stack Recommendations",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF1E1E1E),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {

            recommendations.forEachIndexed { index, rec ->
                ExpandableRecommendationCard(
                    title = "🔹 ${rec.name}",
                    purpose = rec.purpose,
                    roadmap = rec.roadmap,
                    resources = rec.resources,
                    expanded = expandedCardIndex.intValue == index,
                    onClick = {
                        expandedCardIndex.intValue =
                            if (expandedCardIndex.intValue == index) -1 else index
                    },
                    onViewRoadmap = { url -> viewRoadmapUrl = url },
                    viewModel = viewModel,
                    userEmail =userEmail
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("🔁 Start Over", color = Color.White)
            }
        }
    }
}

@Composable

fun ExpandableRecommendationCard(
    title: String,
    purpose: String,
    roadmap: List<String>,
    resources: List<String>,
    expanded: Boolean,
    onClick: () -> Unit,
    onViewRoadmap: (String) -> Unit,
    viewModel: SmartLearningViewModel,
    userEmail: String
)

{val context = LocalContext.current

    val roadmapLinks = mapOf(
        "Frontend Developer" to "https://roadmap.sh/frontend",
        "Backend Developer" to "https://roadmap.sh/backend",
        "Full Stack Developer" to "https://roadmap.sh/full-stack",
        "DevOps" to "https://roadmap.sh/devops",
        "Android Developer" to "https://roadmap.sh/android",
        "iOS Developer" to "https://roadmap.sh/ios",
        "React" to "https://roadmap.sh/react",
        "Angular" to "https://roadmap.sh/angular",
        "Vue.js" to "https://roadmap.sh/vue",
        "Node.js" to "https://roadmap.sh/nodejs",
        "Python Developer" to "https://roadmap.sh/python",
        "Java Developer" to "https://roadmap.sh/java",
        "JavaScript Developer" to "https://roadmap.sh/javascript",
        "TypeScript Developer" to "https://roadmap.sh/typescript",
        "Spring Boot" to "https://roadmap.sh/spring-boot",
        "QA Engineer" to "https://roadmap.sh/qa",
        "Database Administrator" to "https://roadmap.sh/db",
        "Cybersecurity" to "https://roadmap.sh/cyber-security",
        "Blockchain Developer" to "https://roadmap.sh/blockchain",
        "PostgreSQL" to "https://roadmap.sh/postgresql-dba",
        "System Design" to "https://roadmap.sh/system-design",
        "Docker" to "https://roadmap.sh/docker",
        "Kubernetes" to "https://roadmap.sh/kubernetes",
        "AWS" to "https://roadmap.sh/aws"
    )

    val techAliases = mapOf(
        "flutter" to "Android Developer",
        "react native" to "React",
        "ml" to "Python Developer",
        "mobile" to "Android Developer",
        "ai" to "Python Developer",
        "k8s" to "Kubernetes",
        "cloud" to "AWS",
        "frontend" to "Frontend Developer",
        "backend" to "Backend Developer",
        "fullstack" to "Full Stack Developer",
        "qa" to "QA Engineer",
        "cyber" to "Cybersecurity",
        "postgres" to "PostgreSQL"
    )

    val rawTechName = title.removePrefix("🔹").trimStart()

    val aliasMatch = techAliases.entries.find {
        rawTechName.lowercase().contains(it.key)
    }?.value

    val directMatch = roadmapLinks[rawTechName]
    val containsMatch = roadmapLinks.entries.find {
        rawTechName.contains(it.key, ignoreCase = true) || it.key.contains(rawTechName, ignoreCase = true)
    }?.value

    val roadmapUrl = directMatch ?: roadmapLinks[aliasMatch] ?: containsMatch ?: "https://roadmap.sh/"
    Log.d("RoadmapMatch", "Using roadmap URL for $rawTechName -> $roadmapUrl")


    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF3F51B5)
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "🧠 Purpose: $purpose",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                    color = Color(0xFF444444)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "🛠️ Roadmap:",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A237E)
                )
                roadmap.forEachIndexed { i, step ->
                    Text(text = "${i + 1}. $step", color = Color.DarkGray, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "📚 Resources:",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A237E)
                )
                resources.forEach {
                    Text(text = "• $it", color = Color(0xFF2E7D32), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            onViewRoadmap(roadmapUrl)

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                    ) {
                        Text("🌐 View Roadmap", color = Color.White)
                    }


//                    Button(
//                        onClick = {
//                            Log.d("SaveRoadmap", "Clicked Save for $rawTechName - $roadmapUrl")
//                            viewModel.saveRoadmapRecommendation(
//                                userEmail = userEmail,
//                                techName = rawTechName,
//                                roadmapUrl = roadmapUrl,
//                                onSuccess = {
//                                    Toast.makeText(context, "✅ Saved to profile", Toast.LENGTH_SHORT).show()
//                                },
//                                onFailure = {
//                                    Toast.makeText(context, "❌ Failed to save", Toast.LENGTH_SHORT).show()
//                                }
//                            )
//                        },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
//                    ) {
//                        Text("💾 Save to Profile", color = Color.White)
//                    }



                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapWebViewScreen(url: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📍 Roadmap Viewer") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}
