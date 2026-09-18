package com.example.ubapetdata.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ubapetdata.data.AnimalCondition
import com.example.ubapetdata.data.AnimalType
import com.example.ubapetdata.data.Sighting
import com.example.ubapetdata.ui.MapPoint
import com.example.ubapetdata.ui.UbateLocation
import com.example.ubapetdata.ui.colorOr
import com.example.ubapetdata.ui.components.MapHintBanner
import com.example.ubapetdata.ui.detail.SightingDetailSheet
import com.example.ubapetdata.ui.labelEs
import com.example.ubapetdata.ui.report.ReportSightingSheet
import com.example.ubapetdata.ui.theme.OnTeal
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMapScreen(
    sightings: List<Sighting>,
    onAddSighting: (MapPoint, AnimalType, AnimalCondition, String) -> Unit,
    onMarkAttended: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onOpenList: () -> Unit
) {
    val context = LocalContext.current
    val ubate = remember {
        GeoPoint(UbateLocation.LATITUDE, UbateLocation.LONGITUDE)
    }

    var pendingLocation by remember { mutableStateOf<MapPoint?>(null) }
    var selectedSighting by remember { mutableStateOf<Sighting?>(null) }
    var locationEnabled by remember { mutableStateOf(false) }
    var mapViewRef by remember { mutableStateOf<MapView?>(null) }
    var recenterToken by remember { mutableStateOf(0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        locationEnabled = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        val fine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (fine || coarse) {
            locationEnabled = true
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "UbaPet · Ubaté",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = OnTeal
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenList,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = OnTeal,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Mis reportes")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OsmMapView(
                sightings = sightings,
                locationEnabled = locationEnabled,
                ubate = ubate,
                recenterToken = recenterToken,
                onMapReady = { mapViewRef = it },
                onLongPress = { point ->
                    pendingLocation = MapPoint(point.latitude, point.longitude)
                },
                onMarkerClick = { sighting ->
                    selectedSighting = sighting
                }
            )

            MapHintBanner(
                text = "Mantén pulsado el mapa para reportar un animal",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            )

            SmallFloatingActionButton(
                onClick = { recenterToken += 1 },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Centrar en Ubaté")
            }
        }
    }

    pendingLocation?.let { location ->
        ReportSightingSheet(
            location = location,
            onDismiss = { pendingLocation = null },
            onSave = { type, condition, note ->
                onAddSighting(location, type, condition, note)
                pendingLocation = null
            }
        )
    }

    selectedSighting?.let { sighting ->
        val latest = sightings.find { it.id == sighting.id } ?: sighting
        SightingDetailSheet(
            sighting = latest,
            onDismiss = { selectedSighting = null },
            onMarkAttended = {
                onMarkAttended(latest.id)
                selectedSighting = null
            },
            onDelete = {
                onDelete(latest.id)
                selectedSighting = null
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            mapViewRef?.onDetach()
        }
    }
}

@Composable
private fun OsmMapView(
    sightings: List<Sighting>,
    locationEnabled: Boolean,
    ubate: GeoPoint,
    recenterToken: Int,
    onMapReady: (MapView) -> Unit,
    onLongPress: (GeoPoint) -> Unit,
    onMarkerClick: (Sighting) -> Unit
) {
    val context = LocalContext.current
    val mapView = remember {
        createMapView(context, ubate, onLongPress).also(onMapReady)
    }

    LaunchedEffect(recenterToken) {
        if (recenterToken > 0) {
            mapView.controller.setZoom(UbateLocation.DEFAULT_ZOOM.toDouble())
            mapView.controller.animateTo(ubate)
        }
    }

    LaunchedEffect(locationEnabled) {
        val existing = mapView.overlays.filterIsInstance<MyLocationNewOverlay>()
        existing.forEach { mapView.overlays.remove(it) }
        if (locationEnabled) {
            val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
            locationOverlay.enableMyLocation()
            mapView.overlays.add(locationOverlay)
        }
        mapView.invalidate()
    }

    LaunchedEffect(sightings) {
        val toRemove = mapView.overlays.filterIsInstance<Marker>()
        toRemove.forEach { mapView.overlays.remove(it) }

        sightings.forEach { sighting ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(sighting.latitude, sighting.longitude)
                title = sighting.animalType.labelEs()
                snippet = sighting.condition.labelEs()
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                icon = context.getDrawable(org.osmdroid.library.R.drawable.marker_default)?.mutate()
                icon?.setTint(sighting.status.colorOr(sighting.condition).toArgb())
                relatedObject = sighting
                setOnMarkerClickListener { clicked, _ ->
                    val data = clicked.relatedObject as? Sighting
                    if (data != null) onMarkerClick(data)
                    true
                }
            }
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
    }

    DisposableEffect(mapView) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize(),
        update = { it.invalidate() }
    )
}

private fun createMapView(
    context: Context,
    ubate: GeoPoint,
    onLongPress: (GeoPoint) -> Unit
): MapView {
    return MapView(context).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)
        zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        controller.setZoom(UbateLocation.DEFAULT_ZOOM.toDouble())
        controller.setCenter(ubate)
        minZoomLevel = 3.0
        maxZoomLevel = 20.0

        val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean = false

            override fun longPressHelper(p: GeoPoint?): Boolean {
                if (p != null) onLongPress(p)
                return true
            }
        })
        overlays.add(eventsOverlay)
    }
}
