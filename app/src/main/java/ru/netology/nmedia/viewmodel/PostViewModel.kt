package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    author = "",
    authorAvatar = "",
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
    private val _postCreated = SingleLiveEvent<Unit>()//Событие о том, что пост добавлен
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _singleError = SingleLiveEvent<Unit>()//Событие о том, что произошла ошибка
    val singleError: LiveData<Unit>
        get() = _singleError

    init {
        loadPosts()
    }

    fun loadPosts() {
        // Начинаем загрузку
        _data.postValue(FeedModel(loading = true))
        // Данные успешно получены
        repository.getAll(
            object : PostRepository.Callback<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _data.postValue(FeedModel(posts = data, empty = data.isEmpty()))
                }

                // Получена ошибка
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
    }


    fun save() {
        edited.value?.let {
            repository.save(
                it,
                object : PostRepository.Callback<Post> {
                    override fun onSuccess(data: Post) {
                        _postCreated.postValue(Unit)
                    }

                    override fun onError(e: Exception) {
                        // _data.postValue(FeedModel(error = true))
                        _singleError.postValue(Unit)
                    }
                }
            )
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String, video: String?, avatar: String?) {
        val text = content.trim()
        val video = video?.trim()
        val avatar = avatar?.trim()
        if (edited.value?.content == text &&
            edited.value?.videoLink == video &&
            edited.value?.authorAvatar == avatar
        ) {
            return
        }
        edited.value = edited.value?.copy(
            content = text,
            videoLink = video,
            authorAvatar = avatar ?: edited.value?.authorAvatar
        )
    }

    fun likeById(post: Post) {
        val callback = object : PostRepository.Callback<Post> {
            override fun onSuccess(data: Post) {
                val updatedPosts = _data.value?.posts?.map {
                    if (it.id == data.id) {
                        data
                    } else {
                        it
                    }
                }
                _data.postValue(_data.value?.copy(posts = updatedPosts.orEmpty()))
            }

            override fun onError(e: Exception) {
                _singleError.postValue(Unit)
                // _data.postValue(FeedModel(error = true))
            }
        }
        if (post.likedByMe) {
            repository.unlikeById(post.id, callback)
        } else {
            repository.likeById(post.id, callback)
        }
    }

    fun removeById(id: Long) {
        val oldState = _data.value?.posts.orEmpty()
        val updatedPosts = _data.value?.posts.orEmpty().filter {
            it.id != id
        }
        _data.postValue(FeedModel(posts = updatedPosts))

        repository.removeById(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(data: Unit) {}
            override fun onError(e: Exception) {
                _singleError.postValue(Unit)
                _data.postValue(_data.value?.copy(posts = oldState))
            }
        }
        )

    }

    fun cancel() {
        edited.value = empty
    }

    fun shareById(post: Post) {
        val callback = object : PostRepository.Callback<Post> {
            override fun onSuccess(data: Post) {
                val updatedPosts = _data.value?.posts?.map {
                    if (it.id == data.id) {
                        data
                    } else {
                        it
                    }
                }
                _data.postValue(_data.value?.copy(posts = updatedPosts.orEmpty()))
            }

            override fun onError(e: Exception) {
                // _data.postValue(FeedModel(error = true))
                _singleError.postValue(Unit)
            }
        }
        repository.shareById(post.id, callback)
    }
}
