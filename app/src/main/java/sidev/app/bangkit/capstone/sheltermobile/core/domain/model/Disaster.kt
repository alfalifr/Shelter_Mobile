package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import sidev.app.bangkit.capstone.sheltermobile.R

@Parcelize
data class Disaster(
    val id: Int,
    val name: String,
    val imgLink: String,
): Parcelable