package com.demo.myideas.di.module


import android.app.Application
import androidx.room.Room
import com.demo.myideas.data.db.AppDatabase
import com.demo.myideas.data.db.IdeaDao
import com.demo.myideas.data.db.UserInfoDao
import dagger.Module
import dagger.Provides


import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, AppDatabase.DB_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideUserDao(appDataBase: AppDatabase): UserInfoDao{
        return appDataBase.userDao()
    }

    @Provides
    fun provideIdeaDao(appDataBase: AppDatabase): IdeaDao{
        return appDataBase.IdeaDao()
    }
}