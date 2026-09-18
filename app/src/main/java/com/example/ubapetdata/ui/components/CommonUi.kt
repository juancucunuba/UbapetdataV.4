package com.example.ubapetdata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ubapetdata.data.AnimalCondition
import com.example.ubapetdata.data.AnimalType
import com.example.ubapetdata.data.SightingStatus
import com.example.ubapetdata.ui.labelEs
import com.example.ubapetdata.ui.markerColor
import com.example.ubapetdata.ui.theme.OnTeal
import com.example.ubapetdata.ui.theme.StatusAttended

@Composable
fun MapHintBanner(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.92f),
        shadowElevation = 6.dp
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = OnTeal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun ConditionBadge(condition: AnimalCondition, status: SightingStatus) {
    val color = if (status == SightingStatus.ATTENDED) StatusAttended else condition.markerColor()
    val label = if (status == SightingStatus.ATTENDED) status.labelEs() else condition.labelEs()
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
        }
    }
}

@Composable
fun AnimalTypeChips(
    selected: AnimalType,
    onSelected: (AnimalType) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AnimalType.entries.forEach { type ->
            FilterChip(
                selected = selected == type,
                onClick = { onSelected(type) },
                label = { Text(type.labelEs()) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = OnTeal
                )
            )
        }
    }
}

@Composable
fun ConditionChips(
    selected: AnimalCondition,
    onSelected: (AnimalCondition) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AnimalCondition.entries.forEach { condition ->
            val color = condition.markerColor()
            FilterChip(
                selected = selected == condition,
                onClick = { onSelected(condition) },
                label = { Text(condition.labelEs()) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = color,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}
