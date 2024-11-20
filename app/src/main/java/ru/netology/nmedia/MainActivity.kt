package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onCancel() {
                viewModel.cancel()
            }
        }
        )
        binding.container.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val new = adapter.currentList.size < posts.size
            adapter.submitList(posts) {
                if (new) {
                    binding.container.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.edited.observe(this) {
            if (it.id != 0L) {
                binding.group.visibility = View.VISIBLE
                binding.content.setText(it.content)
                binding.content.requestFocus()

            }
        }

        binding.save.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isBlank()) {
                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.changeContent(text)
            binding.group.visibility = View.GONE
            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyBoard(it)
        }
        binding.cancel.setOnClickListener {
            viewModel.cancel()
            binding.group.visibility = View.GONE
            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyBoard(it)
        }
        binding.container.itemAnimator = null

    }
}