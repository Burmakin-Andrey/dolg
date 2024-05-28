package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.TaskDb

@Database(
    entities = [
        TaskDb::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase(){


    abstract fun taskDao(): TasksDao

    companion object {

        private var INSTANCE: LocalDatabase? = null
        private var LOCK = Any()
        private var DB_NAME = "LocalDatabase"

        fun getInstance(context: Context) : LocalDatabase{
            INSTANCE?.let { return it }

            synchronized(LOCK){
                INSTANCE?.let { return it }

                val database = Room.databaseBuilder(
                    context = context,
                    klass = LocalDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = database
                return database
            }
        }
    }
}