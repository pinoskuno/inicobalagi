package com.example.storyappsubmission1.Data.Response

import com.google.gson.annotations.SerializedName

data class GeneralR(
    @field:SerializedName("error") val error: Boolean,
    @field:SerializedName("message") val message: String
)
