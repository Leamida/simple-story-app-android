package com.example.storyapp.presentation.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.map
import com.example.storyapp.core.util.Result
import com.example.storyapp.core.util.getOrAwaitValue
import com.example.storyapp.feature.story.data.repository.StoryRepository
import com.example.storyapp.feature.story.data.source.api.FakeStoryApiService
import com.example.storyapp.feature.story.data.source.api.StoryDataDummy
import com.example.storyapp.feature.story.domain.model.AddStoryResponse
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.feature.story.domain.use_case.AddStoryUseCase
import com.example.storyapp.feature.story.domain.use_case.GetStoriesUseCase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var addStoryUseCase: AddStoryUseCase
    @Mock
    private lateinit var getStoriesUseCase: GetStoriesUseCase
    private lateinit var storiesRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel
    private val dummyStories = StoryDataDummy

    @Before
    fun setUp() {
        storiesRepository = StoryRepository(FakeStoryApiService())
        storyViewModel = StoryViewModel(
            getStoriesUseCase,
            addStoryUseCase
        )
    }

    @Test
    fun `when Get Stories Should Not Null`() {
        val expectedStories = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStories.value = PagingData.from(dummyStories.generateDummyStoriesResponse().listStory)

        `when`(storyViewModel.getStories("token")).thenReturn(expectedStories)

        val actualStories = storyViewModel.getStories("token").getOrAwaitValue()

        Mockito.verify(getStoriesUseCase).invoke("token")


        assertNotNull(actualStories)
    }

    @Test
    fun `when Get Stories with Location Should Not Null and Return Success`() {
        val expectedStories = MutableLiveData<Result<List<ListStoryItem?>?>>()
        expectedStories.value = Result.Success(dummyStories.generateDummyStoriesResponse().listStory)

        `when`(storyViewModel.getStories("token",1)).thenReturn(expectedStories)

        val actualStories = storyViewModel.getStories("token",1).getOrAwaitValue()

        Mockito.verify(getStoriesUseCase).invoke("token",1)

        assertNotNull(actualStories)
        assertTrue(actualStories is Result.Success)
        assertEquals(dummyStories.generateDummyStoriesResponse().listStory,(actualStories as Result.Success).data)
    }

    @Test
    fun `when Add Stories Should Not Null and Return Success`() {
        val description = "test".toRequestBody("text/plain".toMediaType())
        val fakeRequestImageFile = "imageFile".toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "test",
            "test",
            fakeRequestImageFile
        )
        val expectedResponse = MutableLiveData<Result<AddStoryResponse?>?>()
        expectedResponse.value = Result.Success(dummyStories.generateDummyAddStoryResponse())

        `when`(
            storyViewModel.addStory(
                "token",
                imageMultipart,
                description,
                null,
                null
            )
        ).thenReturn(expectedResponse)
        val actualResponse = storyViewModel.addStory("token", imageMultipart, description, null, null).getOrAwaitValue()
        Mockito.verify(addStoryUseCase).invoke("token", imageMultipart, description, null, null)

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(dummyStories.generateDummyAddStoryResponse(),(actualResponse as Result.Success).data)
    }
}