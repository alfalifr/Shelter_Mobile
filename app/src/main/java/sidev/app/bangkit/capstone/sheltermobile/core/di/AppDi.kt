package sidev.app.bangkit.capstone.sheltermobile.core.di

import android.app.Application
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.app.App

object AppDi {
    fun getContext(): Application = App.context
}