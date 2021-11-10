package com.example.cake.network

import com.example.cake.model.CakeModel
import com.example.cake.network.Constants.Companion.BASE_URL
import com.example.cake.network.Constants.Companion.PATH
import retrofit2.http.GET
import retrofit2.http.Path


enum class APIStatus { LOADING, ERROR, DONE }

class Constants {
    companion object {
        const val BASE_URL =
            "https://gist.githubusercontent.com/"
        const val PATH =
            "t-reed/739df99e9d96700f17604a3971e701fa/raw/1d4dd9c5a0ec758ff5ae92b7b13fe4d57d34e1dc/waracle_cake-android-client"
    }
}

interface CakeApi {
    @GET("{baseUrl}/{path}")
    suspend fun getCakeList(
        @Path(
            value = "baseUrl",
            encoded = true
        ) url: String? = BASE_URL,
        @Path(
            value = "path",
            encoded = true
        ) path: String? = PATH
    ): List<CakeModel>
}