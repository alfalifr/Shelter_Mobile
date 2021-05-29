package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency")
data class EmergencyEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val color: String,
    val severity: Int, //higher the value, higher the severity.
)