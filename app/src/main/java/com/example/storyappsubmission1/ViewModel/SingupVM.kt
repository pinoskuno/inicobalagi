package com.example.storyappsubmission1.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappsubmission1.Data.DataBase.StoryRepo

class SingupVM (private val _storyRepo: StoryRepo): ViewModel() {
    fun register(name: String, email: String, pass: String) =
        _storyRepo.postRegister(name, email, pass)

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error
}