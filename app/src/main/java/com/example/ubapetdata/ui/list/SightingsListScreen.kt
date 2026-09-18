package com.example.ubapetdata.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ubapetdata.data.Sighting
import com.example.ubapetdata.ui.components.ConditionBadge
import com.example.ubapetdata.ui.detail.SightingDetailSheet
import com.example.ubapetdata.ui.formatSightingDate
import com.example.ubapetdata.ui.labelEs
import com.example.ubapetdata.ui.theme.OnTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingsListScreen(
    sightings: List<Sighting>,
    onBack: () -> Unit,
    onMarkAttended: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    var selected by remember { mutableStateOf<Sighting?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis reportes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = OnTeal,
                    navigationIconContentColor = OnTeal
                )
            )
        }
    ) { padding ->
        if (sightings.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Aún no hay avistamientos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text(
                    text = "Mantén pulsado el mapa para registrar el primero.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sightings, key = { it.id }) { sighting ->
                    SightingCard(
                        sighting = sighting,
                        onClick = { selected = sighting }
                    )
                }
            }
        }
    }

    selected?.let { sighting ->
        val latest = sightings.find { it.id == sighting.id } ?: return@let
        SightingDetailSheet(
            sighting = latest,
            onDismiss = { selected = null },
            onMarkAttended = {
                onMarkAttended(latest.id)
                selected = null
            },
            onDelete = {
                onDelete(latest.id)
                selected = null
            }
        )
    }
}

@Composable
private fun SightingCard(
    sighting: Sighting,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Pets,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = sighting.animalType.labelEs(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            ConditionBadge(condition = sighting.condition, status = sighting.status)
            Text(
                text = formatSightingDate(sighting.createdAt),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (sighting.note.isNotBlank()) {
                Text(
                    text = sighting.note,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }
        }
    }
}
