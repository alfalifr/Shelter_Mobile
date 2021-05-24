package sidev.app.bangkit.capstone.sheltermobile.data.model

data class WarningStatus(
    val disaster: Disaster,
    val emergency: Emergency,
    val title: String,
    val date: String,
    val location: Location,
)