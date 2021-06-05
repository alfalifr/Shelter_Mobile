package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class TimeString(
    val time: String,
    val pattern: String,
): Parcelable {
    @IgnoredOnParcel
    val timeLong: Long by lazy {
        if(time.isBlank()) 0L
        else {
            val sdf = SimpleDateFormat(pattern, Locale.ROOT)
            sdf.parse(time)!!.time
        }
    }

    operator fun compareTo(other: TimeString): Int = when {
        timeLong < other.timeLong -> -1
        timeLong > other.timeLong -> 1
        else -> 0
    }
}