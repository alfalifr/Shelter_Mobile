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
    //val location: String, //TODO
) {
    override fun equals(other: Any?): Boolean = other is LoginDataResponse
            && other.email == email
            && other.full_name == full_name
            && other.gender == gender
            //&& other.location == location

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + full_name.hashCode()
        result = 31 * result + gender.hashCode()
        //result = 31 * result + location.hashCode()
        return result
    }
}