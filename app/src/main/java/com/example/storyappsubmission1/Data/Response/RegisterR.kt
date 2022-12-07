package com.storyappsubmission1.Data.Response

import com.google.gson.annotations.SerializedName

data class RegisterR(
    @SerializedName("error") var error: Boolean? = false,
    @SerializedName("message") var message: String? = "",

    )
