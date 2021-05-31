package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import java.sql.Timestamp

data class Form(
    val timestamp: Timestamp,
    val title: String,
    val desc: String,
    val photoLinkList: List<String>,
)