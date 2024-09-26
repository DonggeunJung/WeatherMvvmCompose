package com.example.weathermvvmcompose.data.State

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = [State::class], version = 1)
abstract class StateDatabase: RoomDatabase() {
    abstract fun stateDao(): StateDao

    companion object {
        private var instance: StateDatabase? = null

        @Synchronized
        fun getInstance(@ApplicationContext context: Context): StateDatabase {
            if (instance == null) {
                synchronized(StateDatabase::class) {
                    instance = Room.databaseBuilder(
                        context,
                        StateDatabase::class.java,
                        "state-database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}