package com.example.ubapetdata.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ubapetdata.data.AnimalCondition
import com.example.ubapetdata.data.AnimalType
import com.example.ubapetdata.ui.MapPoint
import com.example.ubapetdata.ui.components.AnimalTypeChips
import com.example.ubapetdata.ui.components.ConditionChips

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportSightingSheet(
    location: MapPoint,
    onDismiss: () -> Unit,
    onSave: (AnimalType, AnimalCondition, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var animalType by remember { mutableStateOf(AnimalType.DOG) }
    var condition by remember { mutableStateOf(AnimalCondition.ABANDONED) }
    var note by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Reportar avistamiento",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Ubicación: %.5f, %.5f".format(location.latitude, location.longitude),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text("¿Qué animal viste?", style = MaterialTheme.typography.titleMedium)
            AnimalTypeChips(selected = animalType, onSelected = { animalType = it })

            Text("Estado del animal", style = MaterialTheme.typography.titleMedium)
            ConditionChips(selected = condition, onSelected = { condition = it })

            OutlinedTextField(
                value = note,
                onValueChange = { if (it.length <= 200) note = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nota (opcional)") },
                placeholder = { Text("Ej: cerca del parque, cojea de una pata…") },
                minLines = 3,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { onSave(animalType, condition, note) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Guardar en el mapa")
            }
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
