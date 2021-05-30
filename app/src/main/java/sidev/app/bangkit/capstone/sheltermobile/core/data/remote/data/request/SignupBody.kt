package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

data class SignupBody(
    val _email: String,
    val _password: String,
    val _fname: String,
    val _gender: Char,
): AuthBody() {
    override val _authType = "_register"
}