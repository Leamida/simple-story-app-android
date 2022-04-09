package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.storyapp.core.util.Result
import com.example.storyapp.presentation.view_model.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val storyViewModel: StoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLU5FWTVnRWdoQmNOWE9PbnkiLCJpYXQiOjE2NDk0OTcwNzN9.Era8EWX3r3pXWggJd-2eP96Ba7taIKdl_SZGlNHyGZU"
        storyViewModel.getStories("Bearer $token",size=1)
            .observe(this@MainActivity){ result->
            when(result){
                is Result.Loading->{

                }
                is Result.Success->{
                    Log.d(TAG,result.data.toString())
                }
                is Result.Error->{
                    Log.d(TAG,result.error)
                }
            }
        }
    }

    companion object{
        private val TAG="MainActivity"
    }
}