package com.example.storyapp.presentation.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.storyapp.R
import com.example.storyapp.core.util.EspressoIdlingResource
import com.example.storyapp.core.util.JsonConverter
import com.example.storyapp.di.AppModule
import com.example.storyapp.launchFragmentInHiltContainer
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@MediumTest
class StoriesFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        AppModule.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun getStories_Success() {
        StoriesFragment.token = "token"
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("getstory_success_response.json"))
        mockWebServer.enqueue(mockResponse)
        launchFragmentInHiltContainer<StoriesFragment> {}
        onView(withId(R.id.rvStory))
            .check(matches(isDisplayed()))
        onView(withText("bakekok"))
            .check(matches(isDisplayed()))
    }
}