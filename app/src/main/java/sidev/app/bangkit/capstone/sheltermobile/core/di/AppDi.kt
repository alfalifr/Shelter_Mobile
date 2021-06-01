package sidev.app.bangkit.capstone.sheltermobile.core.di

import android.content.Context
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.app.App

object AppDi {
    fun getContext(): Context = ConfigDi.defaultCtx ?: App.context
}