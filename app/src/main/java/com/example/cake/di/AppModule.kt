package com.example.cake.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.cake.R
import com.example.cake.network.CakeApi
import com.example.cake.network.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build()
    }

    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions.placeholderOf(R.drawable.loading_animation)
            .error(R.drawable.ic_broken)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        application: Application?,
        requestOptions: RequestOptions?
    ): RequestManager {
        return Glide.with(application!!)
            .setDefaultRequestOptions(requestOptions!!)
    }

    @Provides
    fun providesApiService(retrofit: Retrofit): CakeApi {
        return retrofit.create(CakeApi::class.java)
    }
}