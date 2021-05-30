package sidev.app.bangkit.capstone.sheltermobile.core.presentation.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import sidev.lib.android.std.`val`.SuppresLintLiteral

class App: Application() {
    companion object {
        /**
         * It should be safe cause it contains Application context
         */
        @SuppressLint(SuppresLintLiteral.STATIC_FIELD_LEAK, "StaticFieldLeak")
        private var mContext: Application?= null

        /**
         * It should be not null when app is already running.
         */
        val context: Application
            get()= mContext!!
    }

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     *
     *
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     *
     *
     * If you override this method, be sure to call `super.onCreate()`.
     *
     *
     * Be aware that direct boot may also affect callback order on
     * Android [android.os.Build.VERSION_CODES.N] and later devices.
     * Until the user unlocks the device, only direct boot aware components are
     * allowed to run. You should consider that all direct boot unaware
     * components, including such [android.content.ContentProvider], are
     * disabled until user unlock happens, especially when component callback
     * order matters.
     */
    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    /**
     * This method is for use in emulated process environments.  It will
     * never be called on a production Android device, where processes are
     * removed by simply killing them; no user code (including this callback)
     * is executed when doing so.
     */
    override fun onTerminate() {
        super.onTerminate()
        mContext = null
    }
}