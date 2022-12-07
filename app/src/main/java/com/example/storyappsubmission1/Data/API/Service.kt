package com.example.storyappsubmission1.Data.API

import com.example.storyappsubmission1.Data.Response.GeneralR
import com.example.storyappsubmission1.Data.Response.ListStoryR
import com.example.storyappsubmission1.Data.Response.LoginR
import com.example.storyappsubmission1.Data.Response.StoryR
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): GeneralR

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginR

    @Multipart
    @POST("stories")
    fun addStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float
    ): GeneralR
    @GET("stories?location=1")
    suspend fun getStoryWithLocation(
        @Header("Authorization") token: String,
        @Query("size") size: Int
    ): StoryR

    @GET("stories")
    fun getLocation(
        @Header("Authorization") auth: String,
        @Query("location") location: Int
    ): Call<StoryR>
}