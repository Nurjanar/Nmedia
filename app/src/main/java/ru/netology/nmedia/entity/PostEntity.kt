package ru.netology.nmedia.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo()
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val shared: Int = 0,
    val viewed: Int = 0,
    val likedByMe: Boolean,
    val videoLink: String? = null
) {
    fun toDto() = Post(
        id,
        author,
        content,
        published,
        likes,
        shared,
        viewed,
        likedByMe,
        videoLink
    )

    companion object {
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.author,
            post.content,
            post.published,
            post.likes,
            post.shared,
            post.viewed,
            post.likedByMe,
            post.videoLink
        )
    }

}
