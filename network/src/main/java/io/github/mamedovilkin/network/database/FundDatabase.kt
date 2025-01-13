package io.github.mamedovilkin.network.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.mamedovilkin.network.dao.FundDao
import io.github.mamedovilkin.network.model.finex.ListFund

@Database(entities = [ListFund::class], version = 1)
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
                    "funds"
                ).allowMainThreadQueries().build().also {
                    instance = it
                }
            }
        }
    }

}