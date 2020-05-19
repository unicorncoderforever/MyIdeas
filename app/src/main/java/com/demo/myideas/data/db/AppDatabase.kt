package com.demo.myideas.data.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demo.myideas.data.model.Idea
import com.demo.myideas.data.model.UserInfo


@Database(entities = [UserInfo::class,Idea::class], version = AppDatabase.VERSION)

abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ideas.db"
        const val VERSION = 1

        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()

        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                        .databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                        .build()
                }
                return INSTANCE!!
            }
        }
    }

    abstract fun userDao(): UserInfoDao

    abstract fun IdeaDao(): IdeaDao
}