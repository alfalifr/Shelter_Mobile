package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

import com.google.gson.annotations.SerializedName

data class ReportReqBody(
    @SerializedName("from")
    val email: String,
    val msg: String,
    val imgLink: String = "",
) {
    val _feedback = true
    val type = "Urgent"
}