package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import java.sql.Timestamp

data class WeatherForecast(
    val temperature: Float,
    val humidity: Float,
    val rainfall: Float,
    val windSpeed: Float,
    val ultraviolet: Float,
    val timestamp: String,
)

/*
Banjir & longsor:
- Data udah jadian, tinggal diemit.
- Untuk alert, .
*/


