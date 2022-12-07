package com.example.storyappsubmission1.ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission1.Data.DataBase.StoryRepo
import com.example.storyappsubmission1.Data.Functon.Preference
import com.example.storyappsubmission1.Data.Response.LoginResult
import kotlinx.coroutines.launch

class LoginVM(private val _storyRepo: StoryRepo) : ViewModel() {
    fun login(email: String, pass: String) = _storyRepo.postLogin(email,pass)
}