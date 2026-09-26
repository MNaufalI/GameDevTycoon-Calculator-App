package com.naufal.gdtcalculator.data.repository

import com.naufal.gdtcalculator.data.model.Bookmark
import kotlinx.coroutines.flow.Flow

interface IBookmarkRepository {
    fun getBookmarks(): Flow<List<Bookmark>>
    suspend fun addBookmark(bookmark: Bookmark)
    suspend fun removeBookmark(id: String)
    suspend fun isBookmarked(topic: String, primaryGenre: String): Boolean
}