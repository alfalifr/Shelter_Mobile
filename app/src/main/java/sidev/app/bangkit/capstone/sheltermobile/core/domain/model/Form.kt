package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import java.sql.Timestamp

data class Form(
    //val id: Int,
    val timestamp: TimeString,
    val title: String,
    val desc: String,
    val photoLinkList: List<String>,
)