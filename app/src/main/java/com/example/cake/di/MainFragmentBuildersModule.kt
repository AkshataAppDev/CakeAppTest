package com.example.cake.di

import com.example.cake.ui.CakeListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector(modules = [MainViewModelModule::class])
    internal abstract fun contributeCakeListFragment(): CakeListFragment
}