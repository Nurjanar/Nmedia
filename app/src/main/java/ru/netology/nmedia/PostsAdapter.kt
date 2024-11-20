package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
    fun onCancel() {}
}

class PostsAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            CardPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
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
            onInteractionListener.onLike(post)
        }

        share.setOnClickListener {
            onInteractionListener.onShare(post)
        }
        menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.menu_options)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            onInteractionListener.onRemove(post)
                            true
                        }

                        R.id.edit -> {
                            onInteractionListener.onEdit(post)
                            true
                        }

                        else -> false
                    }
                }
            }.show()
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
