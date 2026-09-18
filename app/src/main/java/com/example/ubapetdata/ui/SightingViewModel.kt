package com.example.ubapetdata.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ubapetdata.data.AnimalCondition
import com.example.ubapetdata.data.AnimalType
import com.example.ubapetdata.data.Sighting
import com.example.ubapetdata.data.SightingRepository
import com.example.ubapetdata.data.SightingStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SightingViewModel(private val repository: SightingRepository) : ViewModel() {

    val sightings: StateFlow<List<Sighting>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addSighting(
        latitude: Double,
        longitude: Double,
        animalType: AnimalType,
        condition: AnimalCondition,
        note: String
    ) {
        viewModelScope.launch {
            repository.insert(
                Sighting(
                    latitude = latitude,
                    longitude = longitude,
                    animalType = animalType,
                    condition = condition,
                    note = note.trim(),
                    status = SightingStatus.OPEN
                )
            )
        }
    }

    fun markAttended(id: Long) {
        viewModelScope.launch {
            repository.markAttended(id)
        }
    }

    fun deleteSighting(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun observeSighting(id: Long) = repository.observeById(id)
}

class SightingViewModelFactory(
    private val repository: SightingRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SightingViewModel::class.java)) {
            return SightingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
