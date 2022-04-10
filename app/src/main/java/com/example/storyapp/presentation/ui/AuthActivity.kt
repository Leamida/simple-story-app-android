package com.example.storyapp.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.storyapp.MainActivity
import com.example.storyapp.R
import com.example.storyapp.core.util.Result
import com.example.storyapp.databinding.ActivityAuthBinding
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.presentation.view_model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.postLogin("leo@gmail.com", "leo123").observe(this@AuthActivity) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d(TAG, "Loading...")
                }
                is Result.Success -> {
                    Log.d(TAG, result.data)
                    startActivity(Intent(this@AuthActivity,MainActivity::class.java))
                    this@AuthActivity.finish()
                }
                is Result.Error -> {
                    Log.d(TAG, result.error)
                }
            }
        }
    }

    companion object {
        private const val TAG = "AuthActivity"
    }
}