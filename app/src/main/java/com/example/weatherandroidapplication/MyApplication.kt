package com.example.weatherandroidapplication

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.internal.interop.RealmConfigT

class MyApplication : Application() {

    private var config: RealmConfiguration? = null
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        config = RealmConfiguration
            .Builder()
            .name("weather.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .allowWritesOnUiThread(true)
            .allowWritesOnUiThread(true).build()

        config.let { Realm.setDefaultConfiguration(it) }
    }
}