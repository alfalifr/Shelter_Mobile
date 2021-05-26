package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

data class WarningDetail(
    val status: WarningStatus,
    val desc: String,
    val relatedNews: News,
)