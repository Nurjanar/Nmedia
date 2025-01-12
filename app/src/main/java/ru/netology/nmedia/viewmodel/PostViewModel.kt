package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.*

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
    private val repository: PostRepository = PostRepositoryRoomImpl(
        AppDb.getInstance(application).postDao
    )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
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
//    fun changeContent(content: String, video: String?) {
//        edited.value?.let {
//            repository.save(
//                it.copy(
//                    content = content,
//                    videoLink = video
//                )
//            )
//        }
//        edited.value = empty
//    }

    fun likeById(id: Long) = repository.likeById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun cancel() {
        edited.value = empty
    }
}
