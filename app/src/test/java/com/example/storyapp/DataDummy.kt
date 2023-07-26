package com.example.storyapp

import com.example.storyapp.data.dataClass.StoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryItem> {
        val items: MutableList<StoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryItem(
                photoUrl = "https://picsum.photos/id/${i+5}/250/350",
                createdAt = "2023-05-09T05:30:00.000Z",
                name = "Author_${i+10}",
                description = "Description $i",
                id = i.toString(),
                lon = 0.0,
                lat = 0.0
            )
            items.add(quote)
        }
        return items
    }
}