package com.naufal.gdtcalculator.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.naufal.gdtcalculator.data.model.Bookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// ── Extension for making DataStore instance ─────────────────────
private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "bookmarks")

class BookmarkRepository(private val context: Context) : IBookmarkRepository {

    // Key for making data in DataStore
    private val bookmarksKey = stringPreferencesKey("bookmarks_json")

    // ── take all bookmark as flow ──────────────────────────
    override fun getBookmarks(): Flow<List<Bookmark>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[bookmarksKey] ?: "[]"
            Json.decodeFromString<List<Bookmark>>(json)
        }
    }

    // ── add new bookmark ───────────────────────────────────────
    override suspend fun addBookmark(bookmark: Bookmark) {
        context.dataStore.edit { preferences ->
            val json = preferences[bookmarksKey] ?: "[]"
            val current = Json.decodeFromString<MutableList<Bookmark>>(json)
            current.add(0, bookmark)
            preferences[bookmarksKey] = Json.encodeToString(current)
        }
    }

    // ── delete bookmark on id ──────────────────────────────
    override suspend fun removeBookmark(id: String) {
        context.dataStore.edit { preferences ->
            val json = preferences[bookmarksKey] ?: "[]"
            val current = Json.decodeFromString<MutableList<Bookmark>>(json)
            current.removeAll { it.id == id }
            preferences[bookmarksKey] = Json.encodeToString(current)
        }
    }

    // ── Check if the combination already bookmarked ─────────────────────
    override suspend fun isBookmarked(topic: String, primaryGenre: String): Boolean {
        val json = context.dataStore.data.map { preferences ->
            preferences[bookmarksKey] ?: "[]"
        }
        var result = false
        json.collect { jsonString ->
            val bookmarks = Json.decodeFromString<List<Bookmark>>(jsonString)
            result = bookmarks.any {
                it.topic == topic && it.primaryGenre == primaryGenre
            }
            return@collect
        }
        return result
    }
}