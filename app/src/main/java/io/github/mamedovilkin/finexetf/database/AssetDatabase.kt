package io.github.mamedovilkin.finexetf.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import io.github.mamedovilkin.finexetf.model.database.Asset

@Database(entities = [Asset::class], version = 1)
abstract class AssetDatabase : RoomDatabase() {

    abstract fun getDao(): AssetDao

    companion object {

        @Volatile
        private var instance: AssetDatabase? = null

        fun getDatabase(context: Context): AssetDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AssetDatabase::class.java,
                    "assets"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build().also {
                    instance = it
                }
            }
        }
    }
}