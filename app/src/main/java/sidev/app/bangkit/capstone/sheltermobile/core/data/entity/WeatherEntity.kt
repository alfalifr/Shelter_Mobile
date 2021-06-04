package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val icon: String,
)