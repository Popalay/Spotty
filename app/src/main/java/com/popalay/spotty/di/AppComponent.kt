package com.popalay.spotty.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun plus(module:SessionModule):SessionComponent
}