package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

data class UpdateProfileReqBody(
    val email: String,
    val new_name: String,
    val new_addr: String,
    val new_gender: Char,
    val new_pass: String,
): GeneralRequestBody {
    override val _requestType: String = "updateProfile"
}