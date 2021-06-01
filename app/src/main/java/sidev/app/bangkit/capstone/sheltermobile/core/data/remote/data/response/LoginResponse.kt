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
) {
    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
     * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
     * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
     * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
     * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
     *
     * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
     */
    override fun equals(other: Any?): Boolean = other is LoginDataResponse
            && other.email == email
            && other.full_name == full_name
            && other.gender == gender

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + full_name.hashCode()
        result = 31 * result + gender.hashCode()
        return result
    }
}