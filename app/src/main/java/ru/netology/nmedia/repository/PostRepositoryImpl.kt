package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi.retrofitService
import ru.netology.nmedia.dto.Post


class PostRepositoryImpl : PostRepository {
    override fun getAll(callback: PostRepository.Callback<List<Post>>) {
        retrofitService.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body() ?: emptyList())
                    } else {
                        callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<List<Post>>, e: Throwable) {
                    callback.onError(Exception(e))
                }
            })
    }

    override fun likeById(id: Long, callback: PostRepository.Callback<Post>) {
        retrofitService.likeById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body()!!)
                    } else {
                        callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<Post>, e: Throwable) {
                    callback.onError(Exception(e))
                }
            })
    }

    override fun unlikeById(id: Long, callback: PostRepository.Callback<Post>) {
        retrofitService.unlikeById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body()!!)
                    } else {
                        callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<Post>, e: Throwable) {
                    callback.onError(Exception(e))
                }
            })
    }

    override fun shareById(id: Long, callback: PostRepository.Callback<Post>) {
        retrofitService.shareById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body()!!)
                    } else {
                        callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<Post>, e: Throwable) {
                    callback.onError(Exception(e))
                }
            })
    }

    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
        retrofitService.save(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body()!!)
                    } else {
                        callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<Post>, e: Throwable) {
                    callback.onError(Exception(e))
                }
            })
    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
        retrofitService.removeById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(RuntimeException("${response.code()}: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<Unit>, e: Throwable) {
                    callback.onError(Exception(e))
                }
            })
    }
}