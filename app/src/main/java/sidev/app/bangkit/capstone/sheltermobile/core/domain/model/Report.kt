package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Report(
    val timestamp: TimeString,
    val method: Int,
    val location: Location,
    val form: Form?, //null if `method` == Const.METHOD_CALL
): Parcelable