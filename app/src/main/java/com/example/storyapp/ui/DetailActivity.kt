package com.example.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.data.dataClass.StoryItem
import com.example.storyapp.databinding.ActivityDetailBinding


@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<StoryItem>("STORY") as StoryItem

        binding.apply {
            tvUsernameDetail.text = story.name
            tvCreatedDate.text = story.createdAt
            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .into(tvImageStory)
            tvDescriptionValue.text = story.description
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


