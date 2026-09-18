package com.example.ubapetdata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.ubapetdata.ui.SightingViewModel
import com.example.ubapetdata.ui.SightingViewModelFactory
import com.example.ubapetdata.ui.UbaPetNavHost
import com.example.ubapetdata.ui.theme.UbapetdataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as UbaPetApplication
        val sightingViewModel = ViewModelProvider(
            this,
            SightingViewModelFactory(app.repository)
        )[SightingViewModel::class.java]

        setContent {
            UbapetdataTheme {
                UbaPetNavHost(viewModel = sightingViewModel)
            }
        }
    }
}
