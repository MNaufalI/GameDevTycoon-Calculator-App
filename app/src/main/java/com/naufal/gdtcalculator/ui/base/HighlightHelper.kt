package com.naufal.gdtcalculator.ui.base

import com.naufal.gdtcalculator.data.model.RatingMatrix

object HighlightHelper {
    fun getGenreIndex(genre: String): Int {
        return when (genre) {
            "Action"     -> 0
            "Adventure"  -> 1
            "RPG"        -> 2
            "Simulation" -> 3
            "Strategy"   -> 4
            "Casual"     -> 5
            else         -> -1
        }
    }

    fun getAudienceIndex(audience: String): Int {
        return when (audience) {
            "Young (Y)"    -> 6
            "Everyone (E)" -> 7
            "Mature (M)"   -> 8
            else           -> -1
        }
    }
    fun getRatingForGenre(item: RatingMatrix, genre: String): String {
        return when (genre) {
            "Action"     -> item.action
            "Adventure"  -> item.adventure
            "RPG"        -> item.rpg
            "Simulation" -> item.simulation
            "Strategy"   -> item.strategy
            "Casual"     -> item.casual
            else         -> "+++"
        }
    }

    fun getRatingForAudience(item: RatingMatrix, audience: String): String {
        return when (audience) {
            "Young (Y)"    -> item.audienceYoung
            "Everyone (E)" -> item.audienceEveryone
            "Mature (M)"   -> item.audienceMature
            else           -> "+++"
        }
    }
}