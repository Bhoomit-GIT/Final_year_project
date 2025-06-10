package com.example.slrs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.slrs.data.model.OpenAIQuestion
import com.example.slrs.data.model.TechRecommendation
import com.example.slrs.repository.GeminiRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    private val repository = GeminiRepository()
    private val gson = Gson()

    private val _currentQuestion = MutableStateFlow("")
    private val _options = MutableStateFlow<List<String>>(emptyList())
    private val _loading = MutableStateFlow(false)
    private val _finalResult = MutableStateFlow<String?>(null)
    private val _isTextInputRequired = MutableStateFlow(false)
    private val _textInput = MutableStateFlow("")
    private val _recommendations = MutableStateFlow<List<TechRecommendation>>(emptyList())

    val currentQuestion: StateFlow<String> = _currentQuestion
    val options: StateFlow<List<String>> = _options
    val loading: StateFlow<Boolean> = _loading
    val isTextInputRequired: StateFlow<Boolean> = _isTextInputRequired
    val textInput: StateFlow<String> = _textInput
    val recommendations: StateFlow<List<TechRecommendation>> = _recommendations

    private val userAnswers = mutableListOf<String>()
    private var questionCount = 0
    private var waitingForTextInput = false
    private var isFirstQuestion = true

    init {

            _currentQuestion.value = "What is your current experience level with programming?"
            _options.value = listOf("Beginner", "Intermediate", "Advanced")

    }

    fun answerSelected(optionText: String) {
        if (_loading.value) return

        userAnswers.add(optionText)

        if (isFirstQuestion) {
            isFirstQuestion = false

            if (optionText.lowercase().contains("intermediate") || optionText.lowercase().contains("advanced")) {
                _isTextInputRequired.value = true
                waitingForTextInput = true
                _currentQuestion.value = "Please enter the language or framework you're already familiar with:"
                _options.value = emptyList()
                return
            }
        }

        if (waitingForTextInput) return

        questionCount++
        if (questionCount >= 6) {
            getFinalRecommendation()
        } else {
            askNextQuestion()
        }
    }

    fun submitTextInput(input: String) {
        val trimmedInput = input.trim()

        // Prevent submission if not expecting input or if input is empty
        if (!waitingForTextInput || trimmedInput.isEmpty() || _loading.value) return

        userAnswers.add("Experienced in: $trimmedInput")
        waitingForTextInput = false
        _isTextInputRequired.value = false
        _textInput.value = ""
        questionCount++

        if (questionCount >= 6) {
            getFinalRecommendation()
        } else {
            askNextQuestion()
        }
    }


    fun onTextInputChanged(newText: String) {
        _textInput.value = newText
    }

    private fun askNextQuestion() {
        _loading.value = true
        viewModelScope.launch {
            val prompt = buildQuestionPrompt()
            val response = repository.sendMessage(prompt)

            if (response != null) {
                try {
                    val parsed = gson.fromJson(response.trim(), OpenAIQuestion::class.java)
                    _currentQuestion.value = parsed.question
                    _options.value = parsed.options
                    _loading.value = false
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                    retryAskNextQuestion()
                }
            } else {
                retryAskNextQuestion()
            }
        }
    }

    private fun retryAskNextQuestion() {
        _loading.value = false
        viewModelScope.launch {
            delay(1000)
            askNextQuestion()
        }
    }

    private fun getFinalRecommendation() {
        _loading.value = true
        viewModelScope.launch {
            val prompt = buildFinalPrompt()
            val response = repository.sendMessage(prompt)

            response?.let { raw ->
                try {
                    val recs = raw.trim().split(Regex("\n{2,}")).mapNotNull { section ->
                        val lines = section.lines().map { it.trim() }.filter { it.isNotEmpty() }

                        val name = lines.firstOrNull()?.removeSurrounding("[", "]") ?: return@mapNotNull null
                        val purpose = lines.find { it.startsWith("Purpose:") }?.removePrefix("Purpose:")?.trim() ?: ""
                        val roadmapStart = lines.indexOfFirst { it.startsWith("Roadmap:") }
                        val resourcesStart = lines.indexOfFirst { it.startsWith("Resources:") }

                        if (roadmapStart == -1 || resourcesStart == -1) return@mapNotNull null

                        val roadmap = lines.subList(roadmapStart + 1, resourcesStart)
                            .map { it.replace(Regex("^\\d+\\.\\s*"), "") }

                        val resources = lines.drop(resourcesStart + 1)
                            .map { it.removePrefix("- ").trim() }
                            .filter { it.isNotBlank() }

                        TechRecommendation(
                            name = name,
                            purpose = purpose,
                            roadmap = roadmap,
                            resources = resources
                        )
                    }

                    _recommendations.value = recs
                } catch (e: Exception) {
                    e.printStackTrace()
                    _currentQuestion.value = "⚠️ Couldn't parse tech recommendations."
                    _options.value = listOf("Retry")
                }
            } ?: run {
                _currentQuestion.value = "⚠️  Retrying..."
                _options.value = emptyList()
                retryAskNextQuestion()

            }

            _loading.value = false
        }
    }

    private fun buildQuestionPrompt(): String {
        val answers = userAnswers.joinToString("\n") { "- $it" }
        val beginnerNote = if (userAnswers.size == 1 && userAnswers[0].contains("Beginner", ignoreCase = true)) {
            "\n🟢 The user is a **complete beginner**. Ask a very simple, non-technical question to understand their interest area (e.g., web, mobile, data, design)."
        } else ""

        return """
            You are a tech career assistant helping users choose the **best tech stack to learn in 2025**.

            🎯 Your task:
            Ask **only ONE multiple-choice question (MCQ)** at a time based on the user's level and previous answers.

            🧠 Guidelines:
            - If the user is a **beginner**, ask very simple and basic questions.
            - Do **not** include technical jargon, libraries, or frameworks for beginners.
            - If the user is **experienced**, personalize questions based on their known technologies.
            - Consider the user's background from their answers so far.

            🔐 Format STRICTLY as raw JSON:
            {
              "question": "Your question here",
              "options": ["Option A", "Option B", "Option C", "Option D"]
            }

            ${if (userAnswers.isNotEmpty()) "User's answers so far:\n$answers" else "The user is just starting. Begin with a basic, friendly question."}
            $beginnerNote

            ❌ Do NOT include any extra text or explanation outside the JSON.
        """.trimIndent()
    }

    private fun buildFinalPrompt(): String {
        val answers = userAnswers.joinToString("\n") { "- $it" }

        return """
You are an expert career assistant helping users find the **best tech stacks to learn in 2025**, based on their background, interests, and goals.

🧠 Use the following input to guide your recommendation:
$answers

🎯 Your job is to recommend exactly **3** modern and realistic tech stacks for the user to focus on. These stacks must align with the user's skill level, goals, and current experience.

✅ For each recommendation:
- Pick stacks that are **in-demand** and **relevant in 2025**
- Consider the user's level (Beginner, Intermediate, Advanced)
- Include only stacks they can realistically start learning now

🗂️ If the recommendation matches any of the following **known categories**, use the **exact same name and casing** as shown below:

Frontend  
Backend  
DevOps  
Full Stack  
AI Engineer  
Data Analyst  
AI and Data Scientist  
Android  
iOS  
PostgreSQL  
Blockchain  
QA  
Software Architect  
Cyber Security  
UX Design  
Game Developer  
Technical Writer  
MLOps  
Product Manager  
Engineering Manager  
Developer Relations  
Flutter  
React Native  
ML  
Mobile  
AI  
K8s  
Cloud  
Fullstack  
Cyber  
Postgres

💡 For each recommended tech stack, return:
- ✅ **Name** (match the above list if applicable)
- 🧠 **One-line purpose** (why should the user learn it?)
- 🛠️ **Learning roadmap** with 3 simple steps:
    1. What to learn first
    2. What to build or explore next
    3. Final learning outcome or real-world skill
- 📚 **2–3 Free resources** (websites, tutorials, or courses — no paid content)

📦 Format STRICTLY like this (for every stack):
[Tech Stack Name]  
Purpose: ...  
Roadmap:  
1. ...  
2. ...  
3. ...  
Resources:  
- ...  
- ...  
- ...

⚠️ Do NOT include any extra commentary, greetings, or explanations.  
⚠️ Do NOT number the stacks (like “Stack 1”, “Stack 2”).  
⚠️ Separate each stack with exactly **two new lines**.
""".trimIndent()
    }

    fun restart() {
        userAnswers.clear()
        _currentQuestion.value = ""
        _options.value = emptyList()
        _finalResult.value = null
        _isTextInputRequired.value = false
        _textInput.value = ""
        _recommendations.value = emptyList()
        questionCount = 0
        isFirstQuestion = true
        waitingForTextInput = false
        askNextQuestion()
    }
}
