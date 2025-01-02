package io.github.mamedovilkin.finexetf.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Fund::class], version = 2)
abstract class FundDatabase : RoomDatabase() {

    abstract fun getDao(): FundDao

    companion object {

        @Volatile
        private var instance: FundDatabase? = null

        fun getDatabase(context: Context): FundDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    FundDatabase::class.java,
                    "assets"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build().also {
                    instance = it
                }
            }
        }
    }
}