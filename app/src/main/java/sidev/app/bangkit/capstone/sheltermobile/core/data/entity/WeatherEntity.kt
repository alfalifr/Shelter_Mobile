package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import java.sql.Timestamp

@Entity(tableName = "weather", primaryKeys = ["timestamp", "locationId"])
data class WeatherEntity(
    val timestamp: Timestamp,
    val locationId: Int,
    val temperature: String,
    val humidity: Float,
    val rainfall: Float,
    val windSpeed: Float,
)

/*
Banjir & longsor:
- Data udah jadian, tinggal diemit.
- Untuk alert, .
*/


