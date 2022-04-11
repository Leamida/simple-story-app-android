package com.example.storyapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
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
                it?.token.let { token->
                    if (token.isNullOrEmpty()){
                        startActivity(Intent(this@MainActivity,AuthActivity::class.java))
                        this@MainActivity.finish()
                    }else{
                        getStories(token)
                    }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvStory.layoutManager = LinearLayoutManager(this)

    }

    private fun getStories(token: String) {
        storyViewModel.getStories("Bearer $token", size = 10)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(resources.getString(R.string.logout_ask))
                builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    userViewModel.setUser(null)
                    finish()
                }
                builder.setNegativeButton(resources.getString(R.string.no)) { _, _ ->
                }
                builder.show()
                true
            }
            R.id.settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> true
        }
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}