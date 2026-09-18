package com.example.ubapetdata.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

class Converters {
    @TypeConverter
    fun fromAnimalType(value: AnimalType): String = value.name

    @TypeConverter
    fun toAnimalType(value: String): AnimalType = AnimalType.valueOf(value)

    @TypeConverter
    fun fromAnimalCondition(value: AnimalCondition): String = value.name

    @TypeConverter
    fun toAnimalCondition(value: String): AnimalCondition = AnimalCondition.valueOf(value)

    @TypeConverter
    fun fromSightingStatus(value: SightingStatus): String = value.name

    @TypeConverter
    fun toSightingStatus(value: String): SightingStatus = SightingStatus.valueOf(value)
}

@Database(entities = [Sighting::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sightingDao(): SightingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ubapet.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
