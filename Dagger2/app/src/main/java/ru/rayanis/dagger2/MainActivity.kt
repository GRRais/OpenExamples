package ru.rayanis.dagger2

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import javax.inject.Inject

class MainApp: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}

val Context.appComponent: AppComponent
get() = when(this) {
    is MainApp -> appComponent
    else -> this.applicationContext.appComponent
}

class MainActivity : Activity() {

    @Inject
    lateinit var computer: Computer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }
}