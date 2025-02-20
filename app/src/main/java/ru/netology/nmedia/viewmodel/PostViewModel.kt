package ru.netology.nmedia.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likes = 0,
    shared = 0,
    viewed = 0,
    likedByMe = false,
    videoLink = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            // Начинаем загрузку
            _data.postValue(FeedModel(loading = true))
            try {
                // Данные успешно получены
                val posts = repository.getAll()
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            } catch (e: IOException) {
                // Получена ошибка
                Log.e("FCM", "Error loading posts", e)
                _data.postValue(FeedModel(error = true))
            }
        }
    }


    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String, video: String?) {
        val text = content.trim()
        val video = video?.trim()
        if (edited.value?.content == text &&
            edited.value?.videoLink == video
        ) {
            return
        }
        edited.value = edited.value?.copy(
            content = text,
            videoLink = video
        )
    }

    fun likeById(post: Post) {
        thread {
            try {
                val updatedPost = if (post.likedByMe) {
                    repository.unlikeById(post.id)
                } else {
                    repository.likeById(post.id)
                }
                val updatedPosts = _data.value?.posts?.map {
                    if (it.id == updatedPost.id) {
                        updatedPost
                    } else {
                        it
                    }
                }

                _data.postValue(_data.value?.copy(posts = updatedPosts.orEmpty()))
            } catch (e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        }
    }


    fun removeById(id: Long) {
        thread {
            val oldState = _data.value?.posts.orEmpty()
            val updatedPosts = _data.value?.posts.orEmpty().filter {
                it.id != id
            }
            _data.postValue(FeedModel(posts = updatedPosts))

            try {
                repository.removeById(id)
            } catch (_: Exception) {
                _data.postValue(_data.value?.copy(posts = oldState))
            }
        }
    }

    fun shareById(id: Long) {
        thread {
            val oldState = _data.value
            val updatedPosts = _data.value?.posts?.map { post ->
                if (post.id == id) {
                    post.copy(
                        shared = post.shared + 1
                    )
                } else {
                    post
                }
            }

            _data.postValue(updatedPosts?.let { FeedModel(posts = it) })

            try {
                repository.shareById(id)
            } catch (_: Exception) {
                _data.postValue(oldState)
            }

        }
    }

    fun cancel() {
        edited.value = empty
    }
}
