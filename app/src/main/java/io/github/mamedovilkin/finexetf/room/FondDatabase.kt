package io.github.mamedovilkin.finexetf.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Fond::class], version = 1)
abstract class FondDatabase : RoomDatabase() {

    abstract fun getDao(): FondDao

    companion object {

        @Volatile
        private var instance: FondDatabase? = null

        fun getDatabase(context: Context): FondDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    FondDatabase::class.java,
                    "assets"
                ).allowMainThreadQueries().build().also {
                    instance = it
                }
            }
        }
    }
}