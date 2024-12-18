package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

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
    private val repository: PostRepository = PostRepositoryFile(application)
    val data = repository.getAll()
    var selectedPost = mutableListOf(empty)
    val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun changeContent(content: String, video: String?) {
        edited.value?.let {
            repository.save(
                it.copy(
                    content = content,
                    videoLink = video
                )
            )
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun cancel() {
        edited.value = empty
    }

    fun selectPost(post: Post) {
        selectedPost.clear()
        selectedPost.add(post)
    }

    fun returnSelectedPost(): MutableList<Post> {
        return selectedPost
    }
}