package com.demo.myideas.di.builder

import com.demo.myideas.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector



@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainActivityProviders::class])
    abstract fun bindMainActivity(): MainActivity
}