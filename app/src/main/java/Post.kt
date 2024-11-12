data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int,
    var shared: Int,
    var viewed: Int,
    var likedByMe: Boolean = false
)
