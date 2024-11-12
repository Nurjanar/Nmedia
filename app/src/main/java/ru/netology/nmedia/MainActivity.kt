package ru.netology.nmedia

import Post
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likes = 10999,
            shared = 100,
            viewed = 99999999,
            likedByMe = false
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likes.text = numberCheck(post.likes)
            shared.text = numberCheck(post.shared)
            viewedBy.text = numberCheck(post.viewed)
            updateLike(binding, post)

            like.setOnClickListener {
                post.likedByMe = !post.likedByMe
                updateLike(binding, post)
                if (post.likedByMe) {
                    post.likes++
                } else {
                    post.likes--
                }
                likes.text = numberCheck(post.likes)
            }
            share.setOnClickListener {
                post.shared++
                shared.text = numberCheck(post.shared)
            }
        }

    }

}

private fun updateLike(binding: ActivityMainBinding, post: Post) {
    binding.like.setImageResource(
        if (post.likedByMe) {
            R.drawable.ic_liked_24
        } else {
            R.drawable.ic_like_24
        }
    )
}

private fun numberCheck(count: Int): String {
    var result = ""
    var value = 0.0

    if (count in 0..999) {
        result = count.toString()
    }
    if (count in 1000..9999) {
        value = count / 1000.0
        result = formatDouble(value) + "K"
    }
    if (count in 10000..999999) {
        result = (count / 1000).toString() + "K"
    }
    if (count in 1000000..99999999) {
        value = count / 1000000.0
        result = formatDouble(value) + "M"
    }
    if (count in 100000000..999999999) {
        result = (count / 1000000).toString() + "M"
    }
    return result
}

private fun formatDouble(value: Double): String {
    if (value % 1.0 == 0.0) {
        return value.toInt().toString()
    } else {
        val decimalPart = (value * 10).toInt() % 10
        return "${value.toInt()}.$decimalPart"
    }
}