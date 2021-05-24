package sidev.app.bangkit.capstone.sheltermobile.data.model

data class WarningDetail(
    val status: WarningStatus,
    val desc: String,
    val relatedNews: News,
)