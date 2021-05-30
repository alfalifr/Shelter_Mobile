package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

data class LoginBody(
    val _email: String,
    val _password: String,
): AuthBody() {
    override val _authType: String = "_login"
}