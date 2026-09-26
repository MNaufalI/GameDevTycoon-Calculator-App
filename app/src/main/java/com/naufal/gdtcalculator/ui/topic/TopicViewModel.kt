package com.naufal.gdtcalculator.ui.topic

import androidx.lifecycle.viewModelScope
import com.naufal.gdtcalculator.data.model.Bookmark
import com.naufal.gdtcalculator.data.model.Topic
import com.naufal.gdtcalculator.data.repository.GDTRepository
import com.naufal.gdtcalculator.ui.base.BaseViewModel
import com.naufal.gdtcalculator.ui.base.InterfaceSelectable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class TopicViewModel : BaseViewModel(), InterfaceSelectable<Topic> {

    // ── State: selected topic ──────────────────────────────────────
    private val _selectedTopic = MutableStateFlow<Topic?>(null)
    val selectedTopic: StateFlow<Topic?> = _selectedTopic.asStateFlow()

    // ── State: selected genre ──────────────────────────────────────
    private val _selectedGenre = MutableStateFlow("")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    // ── State: selected second genre ───────────────────────────────
    private val _selectedSecondGenre = MutableStateFlow("")
    val selectedSecondGenre: StateFlow<String> = _selectedSecondGenre.asStateFlow()

    // ── State: selected audiences (multiple) ───────────────────────
    private val _selectedAudience = MutableStateFlow("")
    val selectedAudience: StateFlow<String> = _selectedAudience.asStateFlow()

    // ── Filtered topics — filter by genre AND audience ─────────────
    val filteredTopics: StateFlow<List<Topic>> = combine(
        _searchQuery,
        _selectedGenre,
        _selectedSecondGenre,
        _selectedAudience,
        _selectedTopic
    ) { args ->
        val query      = args[0] as String
        val genre      = args[1] as String
        val secondGenre= args[2] as String
        val audience   = args[3] as String
        val hookedTopic= args[4] as Topic?

        var list = GDTRepository.getTopics()

        // Apply filters
        if (query.isNotBlank()) {
            list = list.filter { it.name.contains(query, ignoreCase = true) }
        }
        if (genre.isNotBlank()) {
            list = list.filter { topic ->
                getRatingForGenre(topic, genre).let { it == "+++" || it == "++" }
            }
        }
        if (secondGenre.isNotBlank()) {
            list = list.filter { topic ->
                getRatingForGenre(topic, secondGenre).let { it == "+++" || it == "++" }
            }
        }
        if (audience.isNotBlank()) {
            list = list.filter { topic ->
                getRatingForAudience(topic, audience).let { it == "+++" || it == "++" }
            }
        }

        if (hookedTopic != null && list.none { it.name == hookedTopic.name }) {
            list = listOf(hookedTopic) + list
        }

        list
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GDTRepository.getTopics()
    )

    // ── Select topic — auto-select genre ───────────────────────────
    override fun onItemSelected(item: Topic) {
        if (_selectedTopic.value?.name == item.name) {
            _selectedTopic.value = null
        } else {
            _selectedTopic.value = item
            autoSelectGenre(item)
        }
    }

    // ── Auto-select genre based on topic ────────────────────────
    private fun autoSelectGenre(topic: Topic) {
        val genres = listOf("Action", "Adventure", "RPG", "Simulation", "Strategy", "Casual")
        val topGenres = genres.filter { genre ->
            getRatingForGenre(topic, genre) == "+++"
        }

        when (topGenres.size) {
            1 -> {
                _selectedGenre.value = topGenres[0]
                _selectedSecondGenre.value = ""
            }
            2 -> {
                _selectedGenre.value = topGenres[0]
                _selectedSecondGenre.value = topGenres[1]
            }
            else -> { }
        }
    }

    // ── Select topic ──────────────────
    fun onTopicSelected(topic: Topic) {
        onItemSelected(topic)
    }

    // ── Select genre (toggle) ──────────────────────────────────────
    fun onGenreSelected(genre: String) {
        _selectedGenre.value = genre
    }

    // ── Select second genre (toggle) ───────────────────────────────
    fun onSecondGenreSelected(genre: String) {
        if (genre == _selectedGenre.value) return
        _selectedSecondGenre.value = if (_selectedSecondGenre.value == genre) "" else genre
    }

    // ── Toggle audience (multiple select) ─────────────────────────
    fun onAudienceSelected(audience: String) {
        _selectedAudience.value = if (_selectedAudience.value == audience) "" else audience
    }

    fun clearAudience() {
        _selectedAudience.value = ""
    }

    fun loadFromBookmark(bookmark: Bookmark) {
        _selectedTopic.value = GDTRepository.getTopics()
            .find { it.name == bookmark.topic }
        _selectedGenre.value = bookmark.primaryGenre
        _selectedSecondGenre.value = bookmark.secondaryGenre
        _selectedAudience.value = bookmark.audience
    }

    // ── Clear all selection ────────────────────────────────────────
    override fun clearSelection() {
        _selectedTopic.value = null
        _selectedGenre.value = ""
        _selectedSecondGenre.value = ""
        _selectedAudience.value = ""
        clearSearch()
    }

    // ── Helper: get rating for genre ────────────────────────────
    private fun getRatingForGenre(topic: Topic, genre: String): String {
        return when (genre) {
            "Action"     -> topic.action
            "Adventure"  -> topic.adventure
            "RPG"        -> topic.rpg
            "Simulation" -> topic.simulation
            "Strategy"   -> topic.strategy
            "Casual"     -> topic.casual
            else         -> ""
        }
    }

    // ── Helper: get rating for audience ─────────────────────────
    private fun getRatingForAudience(topic: Topic, audience: String): String {
        return when (audience) {
            "Young (Y)"    -> topic.audienceYoung
            "Everyone (E)" -> topic.audienceEveryone
            "Mature (M)"   -> topic.audienceMature
            else           -> ""
        }
    }
}