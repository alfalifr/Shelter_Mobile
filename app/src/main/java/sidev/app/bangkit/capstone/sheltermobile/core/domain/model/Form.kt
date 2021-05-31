package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

data class Form(
    val id: Int,
    val title: String,
    val desc: String,
    val photoLinkList: List<String>,
)