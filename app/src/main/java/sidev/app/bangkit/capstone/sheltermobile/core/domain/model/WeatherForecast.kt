package sidev.app.bangkit.capstone.sheltermobile.core.domain.model


data class WeatherForecast(
    val temperature: Float,
    val humidity: Float,
    val rainfall: Float,
    val windSpeed: Float,
    val ultraviolet: Float,
    val timestamp: TimeString,
)

/*
Banjir & longsor:
- Data udah jadian, tinggal diemit.
- Untuk alert, .
*/


