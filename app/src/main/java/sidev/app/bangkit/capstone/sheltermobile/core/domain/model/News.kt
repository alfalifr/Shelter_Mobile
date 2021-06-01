package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import java.sql.Timestamp

data class News(
    val timestamp: String,
    val title: String,
    val briefDesc: String,
    val linkImage: String,
    val link: String,
    val type: Int,
)