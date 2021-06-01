package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

data class News(
    val timestamp: TimeString,
    val title: String,
    val briefDesc: String,
    val linkImage: String,
    val link: String,
    val type: Int,
)