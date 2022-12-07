package com.example.storyappsubmission1.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission1.Data.DataBase.StoryRepo
import com.example.storyappsubmission1.Data.Functon.Preference


class FactoryVM private constructor(private val _storyRepo: StoryRepo) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainVM::class.java) -> {
                return MainVM(_storyRepo) as T
            }
            modelClass.isAssignableFrom(LoginVM::class.java) -> {
                return LoginVM(_storyRepo) as T
            }
            modelClass.isAssignableFrom(SingupVM::class.java) -> {
                return SingupVM(_storyRepo) as T
            }
            modelClass.isAssignableFrom(MapsVM::class.java) -> {
                return MapsVM(_storyRepo) as T
            }
            modelClass.isAssignableFrom(StoryVM::class.java) -> {
                return StoryVM(_storyRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown Viewmodel Class: " + modelClass.name)
        }
    }

}