package ru.netology.nmedia.repository

//class PostRepositoryInMemory : PostRepository {
//    private var nextId = 0L
//    private var posts = listOf(
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
//            published = "21 мая в 18:36",
//            likes = 10999,
//            shared = 100,
//            viewed = 99999999,
//            likedByMe = false
//        ),
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
//            published = "21 мая в 18:36",
//            likes = 0,
//            shared = 123,
//            viewed = 65,
//            likedByMe = false
//        ),
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
//            published = "21 мая в 18:36",
//            likes = 556,
//            shared = 98,
//            viewed = 99999999,
//            likedByMe = false
//        )
//    )
//    private val data = MutableLiveData(posts)
//    override fun getAll(): LiveData<List<Post>> = data
//    override fun likeById(id: Long) {
//        posts = posts.map {
//            if (it.id != id) it else
//                it.copy(
//                    likedByMe = !it.likedByMe,
//                    likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
//                )
//        }
//        data.value = posts
//    }
//
//    override fun shareById(id: Long) {
//        posts = posts.map {
//            if (it.id != id) it else
//                it.copy(
//                    shared = it.shared + 1
//                )
//        }
//        data.value = posts
//    }
//
//    override fun removeById(id: Long) {
//        posts = posts.filter { it.id != id }
//        data.value = posts
//    }
//
//    override fun save(post: Post) {
//        posts = if (post.id == 0L) {
//            listOf(
//                post.copy(
//                    id = nextId++, author = "Me", published = "now"
//                )
//
//            ) + posts
//        } else {
//            posts.map {
//                if (it.id != post.id)
//                    it
//                else
//                    it.copy(content = post.content)
//            }
//        }
//        data.value = posts
//    }
//}



