package com.popalay.spotty.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.popalay.spotty.App
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.data.ImageManager
import com.popalay.spotty.location.LocationManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }

    @Provides
    @Singleton
    fun provideApplication(): App {
        return app
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(true)
        return firebaseDatabase
    }

    @Provides
    @Singleton
    fun provideDataManager(firebaseDb: FirebaseDatabase) = DataManager(FirebaseAuth.getInstance(), firebaseDb)

    @Provides
    @Singleton
    fun provideLocationManager() = LocationManager(app)

    @Provides
    @Singleton
    fun provideImageManager() = ImageManager(app)
}