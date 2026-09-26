package com.naufal.gdtcalculator.ui.platform

import com.naufal.gdtcalculator.data.model.Platform
import com.naufal.gdtcalculator.data.repository.GDTRepository
import com.naufal.gdtcalculator.ui.base.BaseViewModel
import com.naufal.gdtcalculator.ui.base.HighlightHelper
import com.naufal.gdtcalculator.ui.base.InterfaceSelectable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlatformViewModel : BaseViewModel(), InterfaceSelectable<Platform> {

    override fun onItemSelected(item: Platform){
        _selectedPlatform.value = item
    }

    override fun clearSelection() {
        _selectedPlatform.value = null
        clearSearch()
    }

    private val _filteredPlatforms = MutableStateFlow(GDTRepository.getPlatforms())
    val filteredPlatforms: StateFlow<List<Platform>> = _filteredPlatforms.asStateFlow()

    private val _suggestedPlatforms = MutableStateFlow<List<Platform>>(emptyList())
    val suggestedPlatforms: StateFlow<List<Platform>> = _suggestedPlatforms.asStateFlow()

    private val _selectedPlatform = MutableStateFlow<Platform?>(null)
    val selectedPlatform: StateFlow<Platform?> = _selectedPlatform.asStateFlow()

    // ── Generate suggestions based on genre(s) and audience ───────
    fun generateSuggestions(genre: String, secondGenre: String = "", audience: String = "") {
        val allPlatforms = GDTRepository.getPlatforms()
        val isMultiGenre = secondGenre.isNotBlank()

        val suggested = allPlatforms
            .filter { platform ->
                val primaryRating = HighlightHelper.getRatingForGenre(platform, genre)
                val audienceRating = HighlightHelper.getRatingForAudience(platform, audience)

                if (isMultiGenre) {
                    val secondaryRating = HighlightHelper.getRatingForGenre(platform, secondGenre)
                    primaryRating == "+++" && secondaryRating == "+++" && audienceRating == "+++"
                } else {
                    primaryRating == "+++" && audienceRating == "+++"
                }
            }
            .sortedBy { it.name }

        _suggestedPlatforms.value = suggested
        _filteredPlatforms.value = allPlatforms
    }

    override fun onSearchQueryChange(query: String) {
        super.onSearchQueryChange(query)
        _filteredPlatforms.value = if (query.isBlank()) {
            GDTRepository.getPlatforms()
        } else {
            GDTRepository.getPlatforms().filter { platform ->
                platform.name.contains(query, ignoreCase = true)
            }
        }
    }

    fun onPlatformSelected(platform: Platform) {
        _selectedPlatform.value = platform
    }
}