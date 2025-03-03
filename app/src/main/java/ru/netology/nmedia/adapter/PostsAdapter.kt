package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Count
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
    fun onPlayVideo(post: Post) {}
    fun onPostClick(post: Post) {}
    fun openAvatar(post: Post) {}
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
        share.text = count.numberCheck(post.shared)
        viewed.text = count.numberCheck(post.viewed)
        like.isChecked = post.likedByMe
        like.text = count.numberCheck(post.likes)
        if (post.videoLink.isNullOrEmpty()) {
            video.visibility = View.GONE
            playButton.visibility = View.GONE
        } else {
            video.visibility = View.VISIBLE
            video.setImageResource(R.drawable.video)
            playButton.visibility = View.VISIBLE
        }
        val avatarName = post.authorAvatar?.trim().toString()
        if (avatarName.isNotBlank()) {
            val url = "http://10.0.2.2:9999/avatars/$avatarName"
            Glide.with(binding.avatar)
                .load(url)
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_no_avatar_100dp)
                .timeout(10_000)
                .circleCrop()
                .into(binding.avatar)
        } else {
            avatar.setImageResource(R.drawable.ic_no_avatar_100dp)
        }
        playButton.setOnClickListener {
            onInteractionListener.onPlayVideo(post)
        }
        video.setOnClickListener {
            onInteractionListener.onPlayVideo(post)
        }
        like.setOnClickListener {
            onInteractionListener.onLike(post)
        }

        share.setOnClickListener {
            onInteractionListener.onShare(post)
        }
        avatar.setOnClickListener {
            onInteractionListener.openAvatar(post)
        }
        published.setOnClickListener {
            onInteractionListener.onPostClick(post)
        }
        content.setOnClickListener {
            onInteractionListener.onPostClick(post)
        }
        author.setOnClickListener {
            onInteractionListener.onPostClick(post)
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

