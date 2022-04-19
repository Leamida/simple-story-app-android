package com.example.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.presentation.adapter.ListStoryAdapter
import com.example.storyapp.presentation.ui.AuthActivity
import com.example.storyapp.presentation.view_model.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.storyapp.core.util.Internet
import com.example.storyapp.feature.auth.data.source.local.preferences.UserPreferences
import com.example.storyapp.presentation.adapter.LoadingStateAdapter
import com.example.storyapp.presentation.ui.AddStoryActivity
import com.example.storyapp.presentation.ui.MapsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val storyViewModel: StoryViewModel by viewModels()
    private var token: String? = null
    private lateinit var userPreferences : UserPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvStory.layoutManager = LinearLayoutManager(this)

        userPreferences = UserPreferences(applicationContext)

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
            if (Internet.isAvailable(this@MainActivity)){
                binding.cReload.visibility = View.GONE
                getUserToken()
            }else{
                Toast.makeText(
                    this@MainActivity,
                    resources.getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }

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
        userPreferences.getUser().asLiveData().observe(this@MainActivity){
            it?.token.let { token ->
                if (token.isNullOrEmpty()) {
                    startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                    this@MainActivity.finish()
                } else {
                    getStories("Bearer $token")
                    this@MainActivity.token = token
                }
            }
        }
    }

    private fun getStories(token :String) {
        val listStoryAdapter = ListStoryAdapter()
        binding.rvStory.adapter = listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                listStoryAdapter.retry()
            }
        )
        listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
            }
        })
        storyViewModel.getStories(token).observe(this){
            listStoryAdapter.submitData(lifecycle,it)
        }

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
                    CoroutineScope(Dispatchers.IO).launch{
                        userPreferences.setUser(null)
                    }
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
            R.id.findStoryByMap->{
                startActivity(
                    Intent(this@MainActivity, MapsActivity::class.java)
                        .putExtra("token", token)
                )
                finish()
                true
            }
            else -> true
        }
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}