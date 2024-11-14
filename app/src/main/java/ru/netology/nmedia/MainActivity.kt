package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        val count = Count()
        viewModel.post.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likes.text = count.numberCheck(post.likes)
                shared.text = count.numberCheck(post.shared)
                viewedBy.text = count.numberCheck(post.viewed)
                if (post.likedByMe) {
                    like.setImageResource(R.drawable.ic_liked_24)
                }
            }
            with(binding) {
                like.setOnClickListener {
                    viewModel.like()
                    like.setImageResource(
                        if (post.likedByMe)
                            R.drawable.ic_liked_24
                        else
                            R.drawable.ic_like_24
                    )
                    likes.text = count.numberCheck(post.likes)
                }

                share.setOnClickListener {
                    viewModel.share()
                    shared.text = count.numberCheck(post.shared)
                }

            }

        }
    }
}