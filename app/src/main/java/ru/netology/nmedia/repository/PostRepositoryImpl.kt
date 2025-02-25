package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.EMPTY_REQUEST
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(callback: PostRepository.Callback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        makeRequest(
            request,
            callback
        ) {
            gson.fromJson(it, typeToken.type)
        }
    }

    override fun likeById(id: Long, callback: PostRepository.Callback<Post>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .post(EMPTY_REQUEST)
            .build()

        makeRequest(
            request,
            callback
        ) {
            gson.fromJson(it, Post::class.java)
        }
    }

    override fun unlikeById(id: Long, callback: PostRepository.Callback<Post>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .delete()
            .build()

        makeRequest(
            request,
            callback
        ) {
            gson.fromJson(it, Post::class.java)
        }
    }

    override fun shareById(id: Long, callback: PostRepository.Callback<Post>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id/shared")
            .post(EMPTY_REQUEST)
            .build()

        makeRequest(
            request,
            callback
        ) {
            gson.fromJson(it, Post::class.java)
        }
    }

    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        makeRequest(
            request,
            callback
        ) {
            gson.fromJson(it, Post::class.java)
        }
    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        makeRequest(
            request,
            callback
        ) {}
    }

    private fun <T> makeRequest(
        request: Request,
        callback: PostRepository.Callback<T>,
        parse: (String) -> T,
    ) {
        client.newCall(request)
            .enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string() ?: run {
                            callback.onError(RuntimeException("body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(parse(body))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }
                }
            )
    }
}