package com.demo.myideas.di.builder

import com.demo.myideas.SplashFragment
import com.demo.myideas.ui.ideas.AddIdeasFragment
import com.demo.myideas.ui.ideas.IdeasFragment
import com.demo.myideas.ui.login.LoginFragment
import com.demo.myideas.ui.login.SignUpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector



@Module
abstract class MainActivityProviders {
    @ContributesAndroidInjector
    abstract fun provideIdeasFragment(): IdeasFragment

    @ContributesAndroidInjector
    abstract fun provideLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun provideSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    abstract fun provideAddIdeaFragment(): AddIdeasFragment

    @ContributesAndroidInjector
    abstract fun provideSplashFragment(): SplashFragment
}