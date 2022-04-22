package com.example.storyapp.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.MainActivity
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.databinding.FragmentStoriesBinding
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.presentation.adapter.ListStoryAdapter
import com.example.storyapp.presentation.adapter.LoadingStateAdapter
import com.example.storyapp.presentation.view_model.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TOKEN = "token"

@AndroidEntryPoint
class StoriesFragment : Fragment() {

    private lateinit var binding: FragmentStoriesBinding
    private val storyViewModel: StoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoriesBinding.inflate(layoutInflater, container, false)
        binding.rvStory.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            token = it.getString(TOKEN)
        }
        token?.let {
            getStories(it)
        }
        Log.d("StoriesFragment", "called ${token.toString()}")
    }

    private fun getStories(token: String) {
        val listStoryAdapter = ListStoryAdapter()
        binding.rvStory.adapter = listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listStoryAdapter.retry()
            }
        )
        storyViewModel.getStories("Bearer $token").observe(viewLifecycleOwner) {
            listStoryAdapter.submitData(lifecycle, it)
        }

    }


    companion object {
        var token: String? = null
    }
}