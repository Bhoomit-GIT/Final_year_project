package com.example.slrs.repository

import android.util.Log
import com.example.slrs.data.model.ChatCompletionRequest
import com.example.slrs.data.model.ChatCompletionResponse
import com.example.slrs.data.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class GeminiRepository {

    private val api: NetmindAPI

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer 141de36e207e4573b1bd00b2bfa053e2")
                    .addHeader("Content-Type", "application/json")  // ✅ Required!
                    .build()
                chain.proceed(request)
            }
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.netmind.ai/inference-api/openai/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(NetmindAPI::class.java)
    }

    suspend fun sendMessage(prompt: String): String? = withContext(Dispatchers.IO) {
        try {
            val request = ChatCompletionRequest(
                model = "meta-llama/Meta-Llama-3.1-405B", // or "deepseek-chat"
                messages = listOf(
                    ChatMessage(role = "system", content = "Act like you are a helpful assistant."),
                    ChatMessage(role = "user", content = prompt)
                ),
                max_tokens = 20480
            )

            Log.d("API_REQUEST", "Sending prompt:\n$prompt")
            Log.d("API_REQUEST", "Full request: $request")

            val response = api.getChatCompletion(request)
            Log.d("API_RESPONSE", "Response: $response")

            return@withContext response.choices.firstOrNull()?.message?.content
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("API_ERROR", "HTTP ${e.code()} - $errorBody")
            e.printStackTrace()
            return@withContext null
        } catch (e: Exception) {
            Log.e("API_ERROR", "Unexpected error: ${e.localizedMessage}")
            e.printStackTrace()
            return@withContext null
        }
    }


    interface NetmindAPI {
        @Headers("Content-Type: application/json")  // ✅ Add this
        @POST("chat/completions")
        suspend fun getChatCompletion(
            @Body request: ChatCompletionRequest
        ): ChatCompletionResponse
    }

}
