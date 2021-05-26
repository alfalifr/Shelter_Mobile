package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

data class NewsEntity(
    val timestamp: String,
    val title: String,
    val briefDesc: String,
    val linkImage: String,
    val link: String,
    val type: Int,
)