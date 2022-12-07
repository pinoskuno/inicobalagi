package com.example.storyappsubmission1.Data.Functon
import android.app.Application
import com.example.storyappsubmission1.R
import java.io.File

fun createFile(application: Application): File {
    val fileDirectory = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        fileDirectory != null && fileDirectory.exists()
    ) fileDirectory else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}