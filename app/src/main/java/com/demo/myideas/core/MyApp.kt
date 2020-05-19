package com.demo.myideas.core

import com.demo.myideas.di.component.DaggerApplicationComponent
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication



class MyApp : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        Instabug.Builder(this, "028a483ba20847ca8d0ef3156a18d621")
            .setInvocationEvents(
                InstabugInvocationEvent.SHAKE,
                InstabugInvocationEvent.FLOATING_BUTTON
            )
            .build()
    }
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        return DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
    }
}