package com.example.storyapp.feature.story.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp.feature.story.data.MainCoroutineRule
import com.example.storyapp.feature.story.data.source.api.StoryDataDummy
import com.example.storyapp.feature.story.data.source.api.FakeStoryApiService
import com.example.storyapp.feature.story.data.source.api.StoryApiService
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.Assert.*

class StoryRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: StoryApiService
    private lateinit var storiesRepository: StoryRepository

    @Before
    fun setUp() {
        apiService = FakeStoryApiService()
        storiesRepository = StoryRepository(apiService)
    }


    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `when getStory Should Not Null`()  = mainCoroutineRule.runBlockingTest{
        val expectedStories = StoryDataDummy.generateDummyStoriesResponse()
        val actualStories = apiService.getStory("token",null,null)

        assertNotNull(actualStories)
        assertEquals(expectedStories,actualStories)

    }

    @Test
    fun addStory() {
    }
}