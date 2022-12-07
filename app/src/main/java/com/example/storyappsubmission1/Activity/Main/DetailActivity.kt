package com.example.storyappsubmission1.Activity.Main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyappsubmission1.databinding.ActivityDetailBinding
import com.example.storyappsubmission1.Data.Response.ListStoryR


class DetailActivity : AppCompatActivity() {

    private lateinit var  ListStory: ListStoryR
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        ListStory = intent.getParcelableExtra<ListStoryR>(STORIES) as ListStoryR
        binding.apply {
            tvDetailName.text = ListStory.name
            tvDetailPost.text = ListStory.post
            tvDetailDescription.text = ListStory.description
        }
        Glide.with(applicationContext)
            .load(ListStory.photoUrl)
            .into(binding.ivDetailPhoto)
    }
    companion object {
        const val STORIES = "stories"
    }
}