package com.kiselikovgame.radiokp

import android.app.Application
import com.kiselikovgame.radiokp.data.EnclosureDB
import com.kiselikovgame.radiokp.data.FeedDB
import com.kiselikovgame.radiokp.data.FeedItemDB
import com.kiselikovgame.radiokp.utils.createNotificationChannel
import io.realm.kotlin.RealmConfiguration

class App : Application() {

    lateinit var realmConfig : RealmConfiguration

    override fun onCreate() {
        super.onCreate()

        //Create Realm Config and
        realmConfig = RealmConfiguration
            .Builder(schema = setOf(FeedDB::class, FeedItemDB::class, EnclosureDB::class))
            .deleteRealmIfMigrationNeeded()
            .compactOnLaunch()
            .build()

        //Create Notification Channel
        createNotificationChannel(applicationContext)
    }
}