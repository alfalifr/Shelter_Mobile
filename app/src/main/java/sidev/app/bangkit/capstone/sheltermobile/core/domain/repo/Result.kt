package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

sealed class Result<out T> {
    abstract val code: Int
}

data class Success<out T>(
    val data: T,
    override val code: Int,
): Result<T>()

data class Fail(
    val message: String?,
    override val code: Int,
    val error: Throwable?,
): Result<Nothing>()