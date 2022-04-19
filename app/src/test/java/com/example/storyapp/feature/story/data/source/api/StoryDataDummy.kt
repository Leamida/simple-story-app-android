package com.example.storyapp.feature.story.data.source.api

import com.example.storyapp.feature.story.domain.model.AddStoryResponse
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.feature.story.domain.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

object StoryDataDummy {
    fun generateDummyStoriesResponse(): StoryResponse {
        val listStoryItems = ArrayList<ListStoryItem>()

        for (i in 0..10) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1650258638349_88S_QOxm.jpg",
                "2022-04-18T05:10:38.350Z",
                "cara $i",
                "helo",
                -12.0,
                "story-n8By2wkm_y2woMdm$i",
                11.0
            )
            listStoryItems.add(story)
        }

        return StoryResponse(listStoryItems, false, "Stories fetched successfully")
    }

    fun generateDummyAddStoryResponse(
    ) : AddStoryResponse{
       return AddStoryResponse(false, "success add story")
    }
}