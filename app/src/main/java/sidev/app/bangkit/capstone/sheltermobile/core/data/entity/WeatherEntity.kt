package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

data class WeatherEntity(
    val timestamp: String,
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


