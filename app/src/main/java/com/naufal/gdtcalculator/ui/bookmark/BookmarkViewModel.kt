package com.naufal.gdtcalculator.ui.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.gdtcalculator.data.model.Bookmark
import com.naufal.gdtcalculator.data.repository.BookmarkRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BookmarkRepository(application)

    // ── all bookmark as flow ───────────────────────────
    val bookmarks: StateFlow<List<Bookmark>> = repository
        .getBookmarks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ── add bookmark ────────────────────────────────────────────
    fun addBookmark(
        topic: String,
        primaryGenre: String,
        secondaryGenre: String = "",
        audience: String = ""
    ) {
        viewModelScope.launch {
            val bookmark = Bookmark(
                id = UUID.randomUUID().toString(),
                topic = topic,
                primaryGenre = primaryGenre,
                secondaryGenre = secondaryGenre,
                audience = audience
            )
            repository.addBookmark(bookmark)
        }
    }

    // ── Hapus bookmark ─────────────────────────────────────────────
    fun removeBookmark(id: String) {
        viewModelScope.launch {
            repository.removeBookmark(id)
        }
    }

    // ── Toggle bookmark ────────────────────────────────────────────
    fun toggleBookmark(
        topic: String,
        primaryGenre: String,
        secondaryGenre: String = "",
        audience: String = ""
    ) {
        viewModelScope.launch {
            val existing = bookmarks.value.find {
                it.topic == topic && it.primaryGenre == primaryGenre
            }
            if (existing != null) {
                repository.removeBookmark(existing.id)
            } else {
                addBookmark(topic, primaryGenre, secondaryGenre, audience)
            }
        }
    }

    // ── Check if already bookmarked ──────────────────────────────
    fun isBookmarked(topic: String, primaryGenre: String): Boolean {
        return bookmarks.value.any {
            it.topic == topic && it.primaryGenre == primaryGenre
        }
    }
}