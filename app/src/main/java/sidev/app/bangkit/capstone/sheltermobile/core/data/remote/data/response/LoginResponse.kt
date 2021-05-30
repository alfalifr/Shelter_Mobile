package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response

data class LoginResponse(
    val response: String,
    val data: LoginDataResponse,
)

data class LoginDataResponse(
    val id: Int,
    val email: String,
    val full_name: String,
    val gender: Char,
)