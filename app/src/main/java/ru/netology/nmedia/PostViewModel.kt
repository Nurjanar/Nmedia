package ru.netology.nmedia

import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {
    private val repository = PostRepositoryInMemory()
    val post = repository.getPost()

    fun like() {
        repository.like()
    }

    fun share() {
        repository.share()
    }
}