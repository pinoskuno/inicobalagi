package com.example.storyapp.Database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyappsubmission1.Data.Response.ListStoryR


@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryR>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryR>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}