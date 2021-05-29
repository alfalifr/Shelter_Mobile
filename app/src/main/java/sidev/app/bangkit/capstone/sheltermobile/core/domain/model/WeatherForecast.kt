package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import java.sql.Timestamp

data class WeatherForecast(
    val temperature: Double,
    val humidity: Float,
    val rainfall: Float,
    val windSpeed: Float,
    val timestamp: Timestamp,
)

/*
Banjir & longsor:
- Data udah jadian, tinggal diemit.
- Untuk alert, .
*/


