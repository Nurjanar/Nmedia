package ru.netology.nmedia

import Post
import androidx.lifecycle.LiveData

interface PostRepository {
    fun getPost(): LiveData<Post>
    fun like()
    fun share()
}
