package com.example.cake.di

import com.example.cake.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [MainFragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
