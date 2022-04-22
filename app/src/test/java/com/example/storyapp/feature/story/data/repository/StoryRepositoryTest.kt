package com.example.storyapp.feature.story.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp.feature.story.data.MainCoroutineRule
import com.example.storyapp.feature.story.data.source.api.StoryDataDummy
import com.example.storyapp.feature.story.data.source.api.FakeStoryApiService
import com.example.storyapp.feature.story.data.source.api.StoryApiService
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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
        val actualStories = storiesRepository.getStory("token",null,null)

        assertNotNull(actualStories)
        assertEquals(expectedStories,actualStories)

    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `when getStory with location Should Not Null`()  = mainCoroutineRule.runBlockingTest{
        val expectedStories = StoryDataDummy.generateDummyStoriesResponse().listStory
        val actualStories = storiesRepository.getStory("token",1).listStory

        assertNotNull(actualStories)
        assertNotNull(actualStories[0].lat)
        assertNotNull(actualStories[0].lon)
        assertEquals(expectedStories,actualStories)

    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `when success addStory error should be false`() = mainCoroutineRule.runBlockingTest{
        val description ="test".toRequestBody("text/plain".toMediaType())
        val fakeRequestImageFile = "imageFile".toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "test",
            "test",
            fakeRequestImageFile
        )
        val actualResponse = storiesRepository.addStory("token", imageMultipart,description,null,null)
        val expectedError = false
        val actualError = actualResponse.error
        assertEquals(expectedError,actualError)
    }
}