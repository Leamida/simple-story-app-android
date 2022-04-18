package com.example.storyapp.feature.story.data.source.api

import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.feature.story.domain.model.StoryResponse

object StoryDataDummy {
    fun generateDummyStoriesResponse(): StoryResponse {
        val listStoryItems = ArrayList<ListStoryItem>()

        for (i in 0..10){
            val listStoryItem = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1650258638349_88S_QOxm.jpg",
                "2022-04-18T05:10:38.350Z",
                "cara $i",
                "helo",
                null,
                "story-n8By2wkm_y2woMdm$i",
                null
            )
        }

        return StoryResponse(listStoryItems, false, "Stories fetched successfully")
    }
}