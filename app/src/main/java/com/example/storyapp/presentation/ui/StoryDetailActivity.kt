package com.example.storyapp.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.core.util.ShimmerPlaceHolder
import com.example.storyapp.databinding.ActivityStoryDetailBinding
import com.example.storyapp.feature.story.domain.model.ListStoryItem

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>("Data") as ListStoryItem
        Glide.with(applicationContext)
            .load(story.photoUrl)
            .placeholder(ShimmerPlaceHolder.active())
            .into(binding.ivStory)
        binding.apply {
            tvName.text = story.name
            tvDescription.text = story.description
        }
    }
}