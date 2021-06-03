package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Form(
    //val id: Int,
    val timestamp: TimeString,
    val title: String,
    val desc: String,
    val photoLinkList: List<String>,
): Parcelable