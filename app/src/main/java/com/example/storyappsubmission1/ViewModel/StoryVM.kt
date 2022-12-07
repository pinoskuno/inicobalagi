package com.example.storyappsubmission1.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyappsubmission1.Data.DataBase.StoryRepo
import com.example.storyappsubmission1.Data.Functon.Preference
import com.example.storyappsubmission1.Data.Response.LoginResult
import okhttp3.MultipartBody

class StoryVM(private val _storyRepo: StoryRepo) : ViewModel() {
    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: String,
        lat: Double?,
        lon: Double?,
    ) = _storyRepo.addStory(token, file, description, lat, lon)

}