package com.demo.myideas.di.component

import android.app.Application
import com.demo.myideas.core.MyApp
import com.demo.myideas.di.builder.ActivityBuilder
import com.demo.myideas.di.module.ContextModule
import com.demo.myideas.di.module.DatabaseModule
import com.demo.myideas.di.module.NetworkModule
import com.demo.myideas.di.module.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule



import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        RepositoryModule::class,
        DatabaseModule::class,
        ActivityBuilder::class,
        ContextModule::class,
        NetworkModule::class]
)
interface ApplicationComponent : AndroidInjector<MyApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}