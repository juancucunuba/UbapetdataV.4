package com.example.ubapetdata.data

import kotlinx.coroutines.flow.Flow

class SightingRepository(private val dao: SightingDao) {
    fun observeAll(): Flow<List<Sighting>> = dao.observeAll()

    fun observeById(id: Long): Flow<Sighting?> = dao.observeById(id)

    suspend fun getById(id: Long): Sighting? = dao.getById(id)

    suspend fun insert(sighting: Sighting): Long = dao.insert(sighting)

    suspend fun update(sighting: Sighting) = dao.update(sighting)

    suspend fun delete(sighting: Sighting) = dao.delete(sighting)

    suspend fun deleteById(id: Long) = dao.deleteById(id)

    suspend fun markAttended(id: Long) {
        val current = dao.getById(id) ?: return
        dao.update(current.copy(status = SightingStatus.ATTENDED))
    }
}
