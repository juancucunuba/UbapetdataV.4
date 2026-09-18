package com.example.ubapetdata.ui

import androidx.compose.ui.graphics.Color
import com.example.ubapetdata.data.AnimalCondition
import com.example.ubapetdata.data.AnimalType
import com.example.ubapetdata.data.SightingStatus
import com.example.ubapetdata.ui.theme.ConditionAbandoned
import com.example.ubapetdata.ui.theme.ConditionCritical
import com.example.ubapetdata.ui.theme.ConditionInjured
import com.example.ubapetdata.ui.theme.StatusAttended
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun AnimalType.labelEs(): String = when (this) {
    AnimalType.DOG -> "Perrito"
    AnimalType.CAT -> "Gatito"
}

fun AnimalCondition.labelEs(): String = when (this) {
    AnimalCondition.ABANDONED -> "Abandonado"
    AnimalCondition.INJURED -> "Herido"
    AnimalCondition.CRITICAL -> "Estado grave"
}

fun SightingStatus.labelEs(): String = when (this) {
    SightingStatus.OPEN -> "Pendiente"
    SightingStatus.ATTENDED -> "Atendido"
}

fun AnimalCondition.markerColor(): Color = when (this) {
    AnimalCondition.ABANDONED -> ConditionAbandoned
    AnimalCondition.INJURED -> ConditionInjured
    AnimalCondition.CRITICAL -> ConditionCritical
}

fun SightingStatus.colorOr(condition: AnimalCondition): Color =
    if (this == SightingStatus.ATTENDED) StatusAttended else condition.markerColor()

fun formatSightingDate(millis: Long): String {
    val locale = Locale.Builder().setLanguage("es").setRegion("CO").build()
    val formatter = SimpleDateFormat("dd MMM yyyy · HH:mm", locale)
    return formatter.format(Date(millis))
}
