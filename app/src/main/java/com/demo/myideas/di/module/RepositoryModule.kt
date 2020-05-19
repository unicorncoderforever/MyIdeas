package com.demo.myideas.di.module

import com.demo.myideas.data.db.AppDatabase
import com.demo.myideas.data.network.WebService
import com.demo.myideas.data.repository.ideas.IdeaRepositoryImpl
import com.demo.myideas.data.repository.ideas.IdeasRepository
import com.demo.myideas.data.repository.users.UserRepository
import com.demo.myideas.data.repository.users.UserRepositoryImpl

import dagger.Module
import dagger.Provides



import javax.inject.Singleton

@Module(includes = [DatabaseModule::class, NetworkModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(webService: WebService, database: AppDatabase): UserRepository {
        return UserRepositoryImpl(webService, database)
    }

    @Provides
    @Singleton
    fun provideIdeaRepository(webService: WebService, database: AppDatabase): IdeasRepository {
        return IdeaRepositoryImpl(webService, database)
    }
}