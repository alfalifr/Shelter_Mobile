package sidev.app.bangkit.capstone.sheltermobile.core.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Emergency
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.lib.`val`.SuppressLiteral
import java.sql.Timestamp
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object Util {
    private const val DEFAULT_ASYNC_TIMEOUT = 10000L

    fun <T> LiveData<T>.waitForValue(
        timeout: Long = DEFAULT_ASYNC_TIMEOUT,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    ): T {
        val lock = CountDownLatch(1)
        var data: T? = null
        val observer = object: Observer<T> {
            /**
             * Called when the data is changed.
             * @param t  The new data
             */
            override fun onChanged(t: T) {
                data = t
                removeObserver(this)
                lock.countDown()
            }
        }
        observeForever(observer)

        if(!lock.await(timeout, timeUnit)){
            throw TimeoutException("The value was never set.")
        }

        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        return data as T
    }

    fun validateEmail(email: String): Boolean = email.isNotBlank() //TODO Alif: 29 Mei 2021
    fun validatePassword(pswd: String): Boolean = pswd.isNotBlank()

    fun editSharedPref(
        c: Context,
        commit: Boolean = false,
        action: SharedPreferences.Editor.() -> Unit
    ) = c.getSharedPreferences(Const.SHARED_PREF_NAME, Context.MODE_PRIVATE).edit(commit, action)

    fun getSharedPref(c: Context): SharedPreferences = c.getSharedPreferences(Const.SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun getTimestampStr(): String = getTimestamp(null).toString()
    fun getTimestamp(timeStr: String? = null): Timestamp = Timestamp.valueOf(timeStr)

    fun Timestamp.timestampToString(pattern: String = "dd-mm-yyyy"): String = toString() //TODO 29 Mei 2021: pattern blum kepake

    fun getFormattedStr(value: Float, unit: String? = null): String = "%.2f".format(value) + (if(unit != null) " $unit" else "")
    fun getFormattedStr(warning: WarningStatus): String = "Zona ${warning.emergency.name} ${warning.disaster.name}"

    fun getInsertResult(count: Int, totalCount: Int): Result<Int> = when(count) {
        totalCount -> Success(count, 0)
        0 -> cantInsertFailResult()
        else -> Success(count, 1)
    }
    fun failResult(message: String = "Fail", code: Int = -1, error: Throwable?= null): Fail = Fail(message, code, error)
    fun noEntityFailResult(): Fail = failResult("No entity")
    fun noValueFailResult(): Fail = failResult("No value")
    fun cantInsertFailResult(): Fail = failResult("Can't insert")
    fun cantGetFailResult(): Fail = failResult("Can't get")
    fun unknownFailResult(): Fail = failResult("Unknown failure")
    fun operationNotAvailableFailResult(): Fail = failResult("Operation is not available")
    fun operationNotAvailableError(): Nothing = throw IllegalAccessError("Operation is not available")
}