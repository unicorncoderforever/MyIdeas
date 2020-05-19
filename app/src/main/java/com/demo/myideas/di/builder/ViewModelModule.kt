package com.demo.myideas.di.builder

import androidx.lifecycle.ViewModel
import com.demo.myideas.di.qualifier.ViewModelKey
import com.demo.myideas.ui.ideas.IdeasViewModel
import com.demo.myideas.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(IdeasViewModel::class)
    abstract fun bindIdeasViewModel(newsViewModel: IdeasViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(newsViewModel: LoginViewModel): ViewModel

}