package sidev.app.bangkit.capstone.sheltermobile.core.presentation.model

import java.text.SimpleDateFormat
import java.util.*

data class TimeString(
    val time: String,
    val pattern: String,
) {
    val timeLong: Long by lazy {
        val sdf = SimpleDateFormat(pattern, Locale.ROOT)
        sdf.parse(time)!!.time
    }
}