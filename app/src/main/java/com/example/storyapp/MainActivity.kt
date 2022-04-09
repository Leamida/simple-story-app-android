package com.example.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.storyapp.core.util.Result
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.presentation.adapter.ListStoryAdapter
import com.example.storyapp.presentation.ui.AuthActivity
import com.example.storyapp.presentation.view_model.StoryViewModel
import com.example.storyapp.presentation.view_model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val storyViewModel: StoryViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onStart() {
        super.onStart()
        userViewModel.getUser()
            .observe(this@MainActivity) {
                if (it!=null){
                    getStories(it.token)
                }else{
                   startActivity(Intent(this@MainActivity,AuthActivity::class.java))
                       this@MainActivity.finish()
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    private fun getStories(token: String) {
        storyViewModel.getStories("Bearer $token", size = 1)
            .observe(this@MainActivity) { result ->
                when (result) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        result.data?.let {
                            showStoriesOnRecyclerList(it)
                        }
                    }
                    is Result.Error -> {
                        Log.d(TAG, result.error)
                    }
                }
            }
    }

    private fun showStoriesOnRecyclerList(stories:List<ListStoryItem?>) {
        val listStoryAdapter = ListStoryAdapter(stories)
        binding.rvStory.adapter = listStoryAdapter
        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {

            }
        })
    }

//    private fun parcelData(user: ItemsItem) {
//        startActivity(
//            Intent(this@MainActivity, DetailUserActivity::class.java)
//                .putExtra(DetailUserActivity.EXTRA_USER, user)
//                .putExtra(DetailUserActivity.PARCEL_FROM, "MainActivity")
//        )
//    }

    companion object {
        private const val TAG = "MainActivity"
    }
}