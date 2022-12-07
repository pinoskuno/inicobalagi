package com.example.storyappsubmission1.Data.API

import android.content.Context
import com.example.storyapp.Database.StoryDatabase
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.data.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository{
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService, context)
    }
}