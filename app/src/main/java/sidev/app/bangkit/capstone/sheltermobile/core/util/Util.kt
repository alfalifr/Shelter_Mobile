package sidev.app.bangkit.capstone.sheltermobile.core.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.tool.util._FileUtil
import sidev.lib.android.std.tool.util._ResUtil
import sidev.lib.android.std.tool.util.`fun`.asResNameOrNullBy
import sidev.lib.android.std.tool.util.`fun`.imgRes
import sidev.lib.android.std.tool.util.`fun`.loge
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.IllegalStateException
import java.lang.IndexOutOfBoundsException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object Util {
    private const val DEFAULT_ASYNC_TIMEOUT = 10000L
    //private val viewSdf: SimpleDateFormat by lazy { SimpleDateFormat(Const.VIEW_DATE_PATTERN, Locale.ROOT) }
    //private val dbSdf: SimpleDateFormat by lazy { SimpleDateFormat(Const.DB_TIME_PATTERN, Locale.ROOT) }

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

    fun validateName(name: String): Boolean = name.isNotBlank()
    fun validateEmail(email: String): Boolean = Const.REGEX_EMAIL.matches(email)
    fun validatePassword(pswd: String): Boolean = pswd.isNotBlank()

    fun validateFormTitle(title: String): Boolean = title.isNotBlank()
    fun validateFormDesc(desc: String): Boolean = desc.isNotBlank()

    fun editSharedPref(
        c: Context,
        commit: Boolean = false,
        action: SharedPreferences.Editor.() -> Unit
    ) = c.getSharedPreferences(Const.SHARED_PREF_NAME, Context.MODE_PRIVATE).edit(commit, action)

    fun getSharedPref(c: Context): SharedPreferences = c.getSharedPreferences(Const.SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun getTime(): Long = Calendar.getInstance().time.time
    fun getFormattedTimeStr(outPattern: String, timeStr: TimeString?= null, millisOffset: Long = 0): String {
        val sdf = SimpleDateFormat(outPattern, Locale.forLanguageTag("ID"))
        val time = timeStr?.timeLong ?: getTime()
        val date = Date(time + millisOffset)
        return sdf.format(date).also {
//            loge("Util.getFormattedTimeStr() date= $date str= $it millisOffset= $millisOffset time= $time time + millisOffset= ${time + millisOffset}")
        }
    }

    fun getStandardTimeString(timeInStr: String): TimeString = TimeString(timeInStr, Const.DB_TIME_PATTERN)
    fun getStandardTimeString(long: Long): TimeString {
        val sdf = SimpleDateFormat(Const.DB_TIME_PATTERN, Locale.ROOT)
        val date = Date(long)
        val timeStr = sdf.format(date)
        return TimeString(timeStr, Const.DB_TIME_PATTERN)
    }

    fun getDayName(timeStr: TimeString): String = getFormattedTimeStr(Const.DAY_PATTERN, timeStr)
    fun getTimestampStr(timeStr: TimeString? = null, millisOffset: Long = 0): String = getFormattedTimeStr(Const.DB_TIME_PATTERN, timeStr, millisOffset)
    fun getDateStr(timeStr: TimeString): String = getFormattedTimeStr(Const.VIEW_DATE_PATTERN, timeStr)
    fun getDateWithDayStr(timeStr: TimeString): String = getFormattedTimeStr(Const.VIEW_DATE_PATTERN_WITH_DAY, timeStr)

    fun getFormattedStr(value: Float, unit: String? = null, afterComma: Int = 2): String = "%.${afterComma}f".format(value) + (if(unit != null) " $unit" else "")
    fun getFormattedStr(warning: WarningStatus): String =
        if(warning.disaster.name != Const.NO_NAME) "Zona ${warning.emergency.name} ${warning.disaster.name}"
        else "Zona hijau"

    fun getTimeString(inTimeString: TimeString? = null, millisOffset: Long = 0): TimeString {
        //time: String?= null, pattern: String = Const.DB_TIME_PATTERN,
        //loge("Util.getTimeString() time= $time pattern= $pattern millisOffset= $millisOffset")
        val pattern = inTimeString?.pattern ?: Const.DB_TIME_PATTERN
        return TimeString(getTimestampStr(inTimeString, millisOffset = millisOffset), pattern)
    }

    fun getTimeStringStandardOffset(fromTimeStr: TimeString? = null): TimeString = getTimeString(
        fromTimeStr, Const.TIME_STANDARD_OFFSET
    )


    fun getImgLink(c: Context, link: String): Any {
        if(!link.startsWith(Const.PREFIX_DRAWABLE)) return link
        return _ResUtil.getResId(c, link)
    }
    fun ImageView.setImg(link: String) {
        if(!link.startsWith(Const.PREFIX_DRAWABLE)) {
            Glide.with(context)
                .load(link)
                .into(this)
        } else {
            val linkList = link.split(".")
            val id = _ResUtil.getResId(context, linkList[1], linkList[2])
            imgRes = id
        }
    }
    fun ImageView.setGenderImg(gender: Char) = when(gender){
        Const.GENDER_MALE -> imgRes = R.drawable.user_man
        Const.GENDER_FEMALE -> imgRes = R.drawable.user_woman
        else -> throw IllegalArgumentException("No such gender ($gender)")
    }

    fun getExternalFile(c: Context, fileName: String): File? {
        var file = _FileUtil.getExternalFile(c, fileName) ?: return null
        if(file.exists()){
            val lastIndexOfDot = fileName.indexOfLast { it == '.' }
            val prefixFileName = fileName.substring(0, lastIndexOfDot)
            val extension = fileName.substring(lastIndexOfDot)

            var count = 2
            do {
                file = _FileUtil.getExternalFile(c, "${prefixFileName}_$count$extension")
                    ?: return null
                count++
            } while(file.exists())
        }
        return file
    }

    /**
     * [idMap] contains itemId. [idMap] order does matter.
     */
    fun ViewPager2.setWithBnv(bnv: ChipNavigationBar, vararg idMap: Int) {
        var isPageChanging = false
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (!isPageChanging) {
                    isPageChanging = true
                    val itemId = try {
                        idMap[position]
                    } catch (e: IndexOutOfBoundsException) {
                        throw IllegalStateException("No such page index ($position) in `idMap`")
                    }
                    bnv.setItemSelected(itemId)
                    isPageChanging = false
                }
            }
        })
        bnv.setOnItemSelectedListener { itemId ->
            if(!isPageChanging) {
                isPageChanging = true
                val page = idMap.indexOf(itemId)
                if(page < 0)
                    throw IllegalStateException("No such id (${itemId.asResNameOrNullBy(context)}) in `idMap`")
                setCurrentItem(page, true)
                isPageChanging = false
            }
        }
    }

    fun getInsertResult(count: Int, totalCount: Int): Result<Int> = when(count) {
        totalCount -> Success(count, 0)
        0 -> cantInsertFailResult()
        else -> Success(count, 1)
    }
    fun failResult(message: String = "Fail", code: Int = -1, error: Throwable?= null): Fail = Fail(message, code, error)
    fun noEntityFailResult(): Fail = failResult("No entity")
    fun noValueFailResult(): Fail = failResult("No value")
    fun cantInsertFailResult(): Fail = failResult("Can't insert")
    fun cantUpdateFailResult(): Fail = failResult("Can't update")
    fun cantGetFailResult(): Fail = failResult("Can't get")
    fun unknownFailResult(): Fail = failResult("Unknown failure")
    fun operationNotAvailableFailResult(operationName: String = "x"): Fail = failResult("Operation '$operationName' is not available")
    fun operationNotAvailableError(): Nothing = throw IllegalAccessError("Operation is not available")


    fun setAlarm(
        c: Context, pi: PendingIntent,
        hour: Int = 1, minute: Int = 0, second: Int = 0,
        interval: Long = Const.INTERVAL_2_WEEKS,
        repeat: Boolean = true
    ){
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, second)
        val manager = c.getSystemService<AlarmManager>()!!

        if(repeat)
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, interval, pi)
        else
            manager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
    }

    fun stopAlarm(c: Context, pi: PendingIntent){
        pi.cancel()
        c.getSystemService<AlarmManager>()!!.cancel(pi)
    }

    @Suppress(SuppressLiteral.NAME_SHADOWING)
    fun showNotif(
        c: Context,
        smIcon: Int = R.mipmap.ic_launcher_shelter_round,
        title: String = c.getString(R.string.template_title),
        desc: String = c.getString(R.string.template_text),
        channelId: String = "CHANNEL_ID",
        channelName: String = "CHANNEL_NAME",
        channelDesc: String = "CHANNEL_DESC",
        notifId: Int = 1,
        pendingIntent: PendingIntent?= null,
    ){
        val manager = c.getSystemService<NotificationManager>()!!

        val builder = NotificationCompat.Builder(c, channelId)
            .setSmallIcon(smIcon)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setContentTitle(title)
            .setContentText(desc)
            .setAutoCancel(true)

        if(pendingIntent != null)
            builder.setContentIntent(pendingIntent)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = channelDesc

            manager.createNotificationChannel(channel)
        }

        val notif = builder.build()
        manager.notify(notifId, notif)
    }

    suspend fun Bitmap.compressToJpg(): ByteArray {
        val baos = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }
}