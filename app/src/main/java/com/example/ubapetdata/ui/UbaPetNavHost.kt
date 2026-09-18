package com.example.ubapetdata.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ubapetdata.ui.list.SightingsListScreen
import com.example.ubapetdata.ui.map.HomeMapScreen
import com.example.ubapetdata.ui.welcome.WelcomeScreen

object Routes {
    const val WELCOME = "welcome"
    const val MAP = "map"
    const val LIST = "list"
}

@Composable
fun UbaPetNavHost(
    viewModel: SightingViewModel,
    navController: NavHostController = rememberNavController()
) {
    val sightings by viewModel.sightings.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onContinue = {
                    navController.navigate(Routes.MAP) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.MAP) {
            HomeMapScreen(
                sightings = sightings,
                onAddSighting = { point: MapPoint, type, condition, note ->
                    viewModel.addSighting(
                        latitude = point.latitude,
                        longitude = point.longitude,
                        animalType = type,
                        condition = condition,
                        note = note
                    )
                },
                onMarkAttended = viewModel::markAttended,
                onDelete = viewModel::deleteSighting,
                onOpenList = { navController.navigate(Routes.LIST) }
            )
        }
        composable(Routes.LIST) {
            SightingsListScreen(
                sightings = sightings,
                onBack = { navController.popBackStack() },
                onMarkAttended = viewModel::markAttended,
                onDelete = viewModel::deleteSighting
            )
        }
    }
}
