package com.example.storyappsubmission1.ViewModel
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyappsubmission1.Data.API.Config
import com.example.storyappsubmission1.Data.DataBase.StoryRepo
import com.example.storyappsubmission1.Data.Functon.Preference
import com.example.storyappsubmission1.Data.Response.ListStoryR
import com.example.storyappsubmission1.Data.Response.LoginResult
import com.example.storyappsubmission1.Data.Response.StoryR
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainVM (private val _storyRepo: StoryRepo) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun story(token: String): LiveData<PagingData<ListStoryR>> =
        _storyRepo.getStory(token).cachedIn(viewModelScope)
}