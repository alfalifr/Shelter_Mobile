package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

data class LocationEntity(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val parentId: Int, // if null, -1
)