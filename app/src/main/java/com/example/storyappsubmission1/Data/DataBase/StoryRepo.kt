package com.example.storyappsubmission1.Data.DataBase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.Database.StoryDatabase
import com.example.storyappsubmission1.Data.API.Service
import com.example.storyappsubmission1.Data.Response.GeneralR
import com.example.storyappsubmission1.Data.Response.ListStoryR
import com.example.storyappsubmission1.Data.Response.LoginR
import com.example.storyappsubmission1.Data.Response.StoryR
import okhttp3.MultipartBody
import com.example.storyappsubmission1.Data.DataBase.Result

class StoryRepo (private val storyDatabase: StoryDatabase,
                 private val apiService: Service
) {

    fun getStory(token: String): LiveData<PagingData<ListStoryR>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getStoriesWithLocation
                (
        token: String
    ): LiveData<kotlin.Result<StoryR>> = liveData {
        emit(kotlin.Result.Loading)
        try {
            val response = apiService.getStoryWithLocation(token, 50 )
            emit(kotlin.Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(kotlin.Result.Error(e.message.toString()))
        }
    }

    fun addStory(
        token: String,
        image: MultipartBody.Part,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ): LiveData<kotlin.Result<GeneralR>> = liveData {
        emit(kotlin.Result.Loading)
        try {
            val response = apiService.postStory(
                token,
                image,
                description,
                lat,
                lon
            )
            emit(kotlin.Result.Success(response))
        } catch (e: Exception) {
            emit(kotlin.Result.Error(e.message.toString()))
        }
    }

    fun postLogin(email: String, pass: String): LiveData<kotlin.Result<LoginR>> = liveData {
        emit(kotlin.Result.Loading)
        try {
            val response = apiService.postLogin(email, pass)
            emit(kotlin.Result.Success(response))
        } catch (e: java.lang.Exception) {
            Log.d("Login", e.message.toString())
            emit(kotlin.Result.Error(e.message.toString()))
        }
    }

    fun postRegister(
        name: String,
        email: String,
        pass: String
    ): LiveData<kotlin.Result<GeneralR>> = liveData {
        emit(kotlin.Result.Loading)
        try {
            val response = apiService.postRegister(name, email, pass)
            emit(kotlin.Result.Success(response))
        } catch (e: java.lang.Exception) {
            Log.d("Signup", e.message.toString())
            emit(kotlin.Result.Error(e.message.toString()))
        }
    }
}