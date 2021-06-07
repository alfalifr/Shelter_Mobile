package sidev.app.bangkit.capstone.sheltermobile.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.intro.SplashActivity
import sidev.lib.android.std.tool.util.`fun`.loge

class AlarmNotifReceiver: BroadcastReceiver(){
    companion object {
        fun getAlarmPendingIntent(
            c: Context,
            flags: Int = PendingIntent.FLAG_UPDATE_CURRENT,
            title: String = c.getString(R.string.template_title),
            desc: String = c.getString(R.string.template_text),
        ): PendingIntent? {
            val intent = Intent(c, AlarmNotifReceiver::class.java)
            intent.action = Const.ACTION_ALARM_NOTIF
            intent.putExtra(Const.KEY_TITLE, title)
                .putExtra(Const.KEY_DESC, desc)
            return PendingIntent.getBroadcast(c, 0, intent, flags)
        }
        //TODO Alif 7 Juni 2021: Blum kepake
        fun setOn(c: Context, on: Boolean = true){
            if(on){
                Util.setAlarm(
                    c, getAlarmPendingIntent(c)!!, // It's safe to use !! here
                    9, // Set the time for the notification here
                )
            } else {
                val pi = getAlarmPendingIntent(c, PendingIntent.FLAG_NO_CREATE)
                    ?: run {
                        // This could happen when user install update,
                        // so the pendingIntent got erased while the preference still persists in storage.
                        // The resulting pendingIntent will null, so just skip the remaining process.
                        loge("Previous pendingIntent got erased, but the preference still persists in storage, then just return")
                        return
                    }
                Util.stopAlarm(c, pi)
            }
        }
    }
    //TODO Alif 7 Juni 2021: PendingIntent harusnya manggil API tuk ngecek apakah ada warning.
    //Notif muncul saat prediksi dari server terdapat warning merah.
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null){
            if(intent?.action == Const.ACTION_ALARM_NOTIF){
                Util.showNotif(
                    context,
                    title = intent.getStringExtra(Const.KEY_TITLE)!!,
                    desc = intent.getStringExtra(Const.KEY_DESC)!!,
                    pendingIntent = PendingIntent.getActivity(
                        context, 0,
                        Intent(context, SplashActivity::class.java),
                        PendingIntent.FLAG_ONE_SHOT
                    )
                )
                //context.toast("Alarm lewat bro")
            }
        }
    }
}