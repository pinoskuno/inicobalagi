package com.example.storyappsubmission1.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappsubmission1.Data.API.Config
import com.example.storyappsubmission1.Data.DataBase.StoryRepo
import com.example.storyappsubmission1.Data.Response.ListStoryR
import com.example.storyappsubmission1.Data.Response.StoryR
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsVM (private val _storyRepo: StoryRepo):ViewModel() {
    private val _storyWithLocation = MutableLiveData<List<ListStoryR>>()
    val storyWithLocation: LiveData<List<ListStoryR>> = _storyWithLocation


    fun getLocationStory(token: String){
        val client = Config.getApiService().getLocation("Bearer $token", 1)
        client.enqueue(object : Callback<StoryR> {
            override fun onResponse(
                call: Call<StoryR>,
                response: Response<StoryR>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    _storyWithLocation.value = responseBody.listStory
                }
            }

            override fun onFailure(call: Call<StoryR>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }


    companion object{
        private const val TAG = "MapsViewModel"
    }

}