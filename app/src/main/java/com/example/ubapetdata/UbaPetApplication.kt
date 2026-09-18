package com.example.ubapetdata

import android.app.Application
import com.example.ubapetdata.data.AppDatabase
import com.example.ubapetdata.data.SightingRepository
import org.osmdroid.config.Configuration

class UbaPetApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { SightingRepository(database.sightingDao()) }

    override fun onCreate() {
        super.onCreate()
        val prefs = getSharedPreferences("osmdroid", MODE_PRIVATE)
        Configuration.getInstance().load(this, prefs)
        Configuration.getInstance().userAgentValue = packageName
    }
}
