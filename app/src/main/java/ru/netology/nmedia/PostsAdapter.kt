package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding

typealias LikeCallBack = (Post) -> Unit

class PostsAdapter(private val callback: LikeCallBack) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            CardPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            callback
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val callback: LikeCallBack
) : RecyclerView.ViewHolder(binding.root) {
    private val count = Count()
    fun bind(post: Post) = with(binding) {
        author.text = post.author
        published.text = post.published
        content.text = post.content
        likes.text = count.numberCheck(post.likes)
        shared.text = count.numberCheck(post.shared)
        viewedBy.text = count.numberCheck(post.viewed)
        like.setImageResource(
            if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
        )

        like.setOnClickListener {
            callback(post)
        }
        likes.text = count.numberCheck(post.likes)


        share.setOnClickListener {
            post.incrementShared()
            shared.text = count.numberCheck(post.shared)
        }

    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(
        oldItem: Post, newItem: Post
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Post, newItem: Post
    ) = oldItem == newItem
}
