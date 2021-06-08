package sidev.app.bangkit.capstone.sheltermobile.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.runBlocking
import sidev.app.bangkit.capstone.sheltermobile.MainActivity
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.di.RepoDi
import sidev.app.bangkit.capstone.sheltermobile.core.di.UseCaseDi
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.WarningRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase.DashboardUseCase
import sidev.app.bangkit.capstone.sheltermobile.core.util.CaptionMapper
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.intro.SplashActivity
import sidev.lib.android.std.tool.util.`fun`.loge

class AlarmNotifReceiver: BroadcastReceiver(){
    private val dashboardUseCase: DashboardUseCase by lazy { UseCaseDi.getDashbosrdUseCase() }

    companion object {
        fun getAlarmPendingIntent(c: Context, flags: Int = PendingIntent.FLAG_UPDATE_CURRENT,): PendingIntent? {
            val intent = Intent(c, AlarmNotifReceiver::class.java)
            intent.action = Const.ACTION_ALARM_NOTIF
/*
            intent.putExtra(Const.KEY_TITLE, title)
                .putExtra(Const.KEY_DESC, desc)
 */
            return PendingIntent.getBroadcast(c, 0, intent, flags)
        }

        /**
         * returns `true` if the alarm was `off` previously
         */
        fun setOn(c: Context, on: Boolean = true): Boolean {
            return if(on){
                val pi = getAlarmPendingIntent(c, PendingIntent.FLAG_NO_CREATE)
                if(pi != null){
                    Util.setAlarm(
                        c, pi,
                        9, // Set the time for the notification here
                        interval = Const.INTERVAL_2_WEEKS,
                    )
                    true
                } else false
            } else {
                val pi = getAlarmPendingIntent(c, PendingIntent.FLAG_NO_CREATE)
                    ?: run {
                        // This could happen when user install update,
                        // so the pendingIntent got erased while the preference still persists in storage.
                        // The resulting pendingIntent will null, so just skip the remaining process.
                        loge("Previous pendingIntent got erased, but the preference still persists in storage, then just return")
                        return false
                    }
                Util.stopAlarm(c, pi)
                false
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null){
            if(intent?.action == Const.ACTION_ALARM_NOTIF){
                runBlocking {
                    val timeStr = Util.getTimeString()

                    val locRes = dashboardUseCase.getCurrentLocation()
                    if(locRes !is Success) return@runBlocking

                    val weatherRes = dashboardUseCase.getWeatherForecast(timeStr, locRes.data.id)
                    if(weatherRes !is Success) return@runBlocking

                    val res = dashboardUseCase.getDisasterGroupList(timeStr)
                    if(res !is Success) return@runBlocking

                    val highlightedRes = dashboardUseCase.getHighlightedWarningStatus(timeStr)
                    if(highlightedRes !is Success) return@runBlocking

                    val highlightedWarning = highlightedRes.data
                    val highlightedEmergency = highlightedWarning.emergency
                    if(highlightedEmergency.severity == Const.Emergency.SEVERITY_GREEN) return@runBlocking

                    val caption = CaptionMapper.WarningStatus.getCaption(
                        highlightedWarning.disaster,
                        highlightedEmergency
                    )

                    Util.showNotif(
                        context,
                        title = caption.title, //intent.getStringExtra(Const.KEY_TITLE)!!,
                        desc = caption.desc, //intent.getStringExtra(Const.KEY_DESC)!!,
                        pendingIntent = PendingIntent.getActivity(
                            context, 0,
                            Intent(context, MainActivity::class.java),
                            PendingIntent.FLAG_ONE_SHOT
                        )
                    )
                    val broadcast = Intent(Const.ACTION_ALARM_NOTIF_ACT)
                    context.sendBroadcast(broadcast)
                }
            }
        }
    }
}