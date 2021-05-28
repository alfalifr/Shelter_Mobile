package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Emergency(
    val id: Int,
    val name: String,
    val color: String,
    val severity: Int, //higher the value, higher the severity.
): Parcelable