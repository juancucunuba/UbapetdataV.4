package com.example.ubapetdata.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SightingDao {
    @Query("SELECT * FROM sightings ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Sighting>>

    @Query("SELECT * FROM sightings WHERE id = :id")
    fun observeById(id: Long): Flow<Sighting?>

    @Query("SELECT * FROM sightings WHERE id = :id")
    suspend fun getById(id: Long): Sighting?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sighting: Sighting): Long

    @Update
    suspend fun update(sighting: Sighting)

    @Delete
    suspend fun delete(sighting: Sighting)

    @Query("DELETE FROM sightings WHERE id = :id")
    suspend fun deleteById(id: Long)
}
