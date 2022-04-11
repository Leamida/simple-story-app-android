package com.example.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.core.util.Result
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.presentation.adapter.ListStoryAdapter
import com.example.storyapp.presentation.ui.AuthActivity
import com.example.storyapp.presentation.view_model.StoryViewModel
import com.example.storyapp.presentation.view_model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.storyapp.core.util.Internet
import com.example.storyapp.presentation.ui.AddStoryActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val storyViewModel: StoryViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var token: String? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvStory.layoutManager = LinearLayoutManager(this)

        if (Internet.isAvailable(this@MainActivity)) {
            getUserToken()
        } else {
            binding.cReload.visibility = View.VISIBLE
            Toast.makeText(
                this@MainActivity,
                resources.getString(R.string.no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.ibReload.setOnClickListener {
            getUserToken()
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, AddStoryActivity::class.java)
                    .putExtra("token", token)
            )
            finish()
        }
    }

    private fun getUserToken() {
        userViewModel.getUser()
            .observe(this@MainActivity) {
                it?.token.let { token ->
                    if (token.isNullOrEmpty()) {
                        startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                        this@MainActivity.finish()
                    } else {
                        getStories(token)
                        this@MainActivity.token = token
                    }
                }
            }
    }

    private fun getStories(token: String) {
        storyViewModel.getStories("Bearer $token", size = 10)
            .observe(this@MainActivity) { result ->
                when (result) {
                    is Result.Loading -> {
                        Log.d(TAG, "loading...")
                        binding.apply {
                            ibReload.visibility = View.GONE
                            pbMain.visibility = View.VISIBLE
                        }
                    }
                    is Result.Success -> {
                        binding.cReload.visibility = View.GONE
                        if (result.data.isNullOrEmpty()) {
                            binding.apply {
                                cReload.visibility = View.VISIBLE
                                ibReload.visibility = View.VISIBLE
                                pbMain.visibility = View.GONE
                            }
                            Toast.makeText(
                                this@MainActivity,
                                resources.getString(R.string.no_data),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            binding.cReload.visibility = View.GONE
                            showStoriesOnRecyclerList(result.data)
                        }
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            this@MainActivity,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, result.error)
                        binding.apply {
                            ibReload.visibility = View.VISIBLE
                            pbMain.visibility = View.GONE
                        }
                    }
                }
            }
    }

    private fun showStoriesOnRecyclerList(stories: List<ListStoryItem?>) {
        val listStoryAdapter = ListStoryAdapter(stories)
        binding.rvStory.adapter = listStoryAdapter
        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
            }
        })
    }

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