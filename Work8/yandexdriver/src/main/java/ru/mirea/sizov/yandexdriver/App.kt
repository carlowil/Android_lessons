package ru.mirea.sizov.yandexdriver

import android.app.Application
import com.yandex.mapkit.MapKitFactory


class App : Application() {
    private val MAPKIT_API_KEY = "3dc07815-20ce-4a31-a3d2-3460e05a1298"

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }
}