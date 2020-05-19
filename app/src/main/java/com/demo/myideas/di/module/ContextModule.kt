package com.demo.myideas.di.module

import android.app.Application
import android.content.Context
import com.demo.myideas.di.builder.ViewModelBuilder
import dagger.Module
import dagger.Provides


import javax.inject.Singleton

@Module(includes = [ViewModelBuilder::class])
class ContextModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }
}