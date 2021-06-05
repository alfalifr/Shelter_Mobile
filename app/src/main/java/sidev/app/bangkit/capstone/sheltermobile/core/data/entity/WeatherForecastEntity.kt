package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import java.sql.Timestamp

@Entity(tableName = "weather_forecast", primaryKeys = ["timestamp", "locationId"])
data class WeatherForecastEntity(
    val timestamp: Long,
    val locationId: Int,
    val temperature: Float,
    val humidity: Float,
    val rainfall: Float,
    val windSpeed: Float,
    val ultraviolet: Float,
    val weatherId: Int,
)

/*
Banjir & longsor:
- Data udah jadian, tinggal diemit.
- Untuk alert, .
*/


