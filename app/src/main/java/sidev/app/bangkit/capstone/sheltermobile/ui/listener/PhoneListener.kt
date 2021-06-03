package sidev.app.bangkit.capstone.sheltermobile.ui.listener

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import sidev.lib.android.std.tool.util.`fun`.loge

class PhoneListener(private val onAnswer: (incomingPhoneNumber: String?) -> Unit): PhoneStateListener() {
    /**
     * Callback invoked when device call state changes.
     *
     *
     * Reports the state of Telephony (mobile) calls on the device for the registered subscription.
     *
     *
     * Note: the registration subId comes from [TelephonyManager] object which registers
     * PhoneStateListener by [TelephonyManager.listen].
     * If this TelephonyManager object was created with
     * [TelephonyManager.createForSubscriptionId], then the callback applies to the
     * subId. Otherwise, this callback applies to
     * [SubscriptionManager.getDefaultSubscriptionId].
     *
     *
     * Note: The state returned here may differ from that returned by
     * [TelephonyManager.getCallState]. Receivers of this callback should be aware that
     * calling [TelephonyManager.getCallState] from within this callback may return a
     * different state than the callback reports.
     *
     * @param state call state
     * @param incomingPhoneNumber call phone number. If application does not have
     * [READ_CALL_LOG][android.Manifest.permission.READ_CALL_LOG] permission or carrier
     * privileges (see [TelephonyManager.hasCarrierPrivileges]), an empty string will be
     * passed as an argument.
     */
    override fun onCallStateChanged(state: Int, incomingPhoneNumber: String?) {
        //super.onCallStateChanged(state, phoneNumber)
        loge("PhoneListener.onCallStateChanged() state= $state incomingPhoneNumber= $incomingPhoneNumber")
        when(state) {
            TelephonyManager.CALL_STATE_IDLE -> loge("PhoneListener.onCallStateChanged() CALL_STATE_IDLE incomingPhoneNumber= $incomingPhoneNumber")
            TelephonyManager.CALL_STATE_RINGING -> loge("PhoneListener.onCallStateChanged() CALL_STATE_RINGING incomingPhoneNumber= $incomingPhoneNumber")
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                onAnswer(incomingPhoneNumber)
                loge("PhoneListener.onCallStateChanged() CALL_STATE_OFFHOOK incomingPhoneNumber= $incomingPhoneNumber")
            }
        }
    }

}