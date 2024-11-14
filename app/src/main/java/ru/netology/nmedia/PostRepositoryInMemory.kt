package ru.netology.nmedia

import Post
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemory : PostRepository {
    private val data = MutableLiveData(
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likes = 10999,
            shared = 100,
            viewed = 99999999,
            likedByMe = false
        )
    )

    override fun getPost(): LiveData<Post> = data
    override fun like() {
        val currentPost = data.value ?: return
        val updatedPost = currentPost.copy(
            likedByMe = !currentPost.likedByMe,
            likes = if (currentPost.likedByMe) currentPost.likes++ else currentPost.likes--
        )
        data.value = updatedPost
    }

    override fun share() {
        val currentPost = data.value ?: return
        val updatedPost = currentPost.copy(
            shared = ++currentPost.shared
        )
        data.value = updatedPost
    }
}


