package com.naufal.gdtcalculator.data.model

import androidx.compose.runtime.Immutable

interface RatingMatrix {
    val name: String
    val action: String
    val adventure: String
    val rpg: String
    val simulation: String
    val strategy: String
    val casual: String
    val audienceYoung: String
    val audienceEveryone: String
    val audienceMature: String
}

interface DevData {
    val tdRatio: Double
    fun getSlidersForStage(stage: Int): List<Pair<String, String>>
}

@Immutable
data class Topic(
    override val name: String,
    override val action: String,
    override val adventure: String,
    override val rpg: String,
    override val simulation: String,
    override val strategy: String,
    override val casual: String,
    override val audienceYoung: String,
    override val audienceEveryone: String,
    override val audienceMature: String
) : RatingMatrix

@Immutable
data class Platform(
    override val name: String,
    override val action: String,
    override val adventure: String,
    override val rpg: String,
    override val simulation: String,
    override val strategy: String,
    override val casual: String,
    override val audienceYoung: String,
    override val audienceEveryone: String,
    override val audienceMature: String
) : RatingMatrix

@Immutable
data class SingleGenreDev(
    val genre: String,
    override val tdRatio: Double,
    val engine: String,
    val gameplay: String,
    val storyQuests: String,
    val dialogues: String,
    val levelDesign: String,
    val ai: String,
    val worldDesign: String,
    val graphics: String,
    val sound: String
) : DevData {
    override fun getSlidersForStage(stage: Int): List<Pair<String, String>> {
        return when (stage) {
            1 -> listOf(
                "Engine"       to engine,
                "Gameplay"     to gameplay,
                "Story/Quests" to storyQuests
            )
            2 -> listOf(
                "Dialogues"    to dialogues,
                "Level Design" to levelDesign,
                "AI"           to ai
            )
            3 -> listOf(
                "World Design" to worldDesign,
                "Graphics"     to graphics,
                "Sound"        to sound
            )
            else -> emptyList()
        }
    }
}

@Immutable
data class MultiGenreDev(
    val genreCombination: String,
    override val tdRatio: Double,
    val engine: String,
    val gameplay: String,
    val storyQuests: String,
    val dialogues: String,
    val levelDesign: String,
    val ai: String,
    val worldDesign: String,
    val graphics: String,
    val sound: String
) : DevData {
    override fun getSlidersForStage(stage: Int): List<Pair<String, String>> {
        return when (stage) {
            1 -> listOf(
                "Engine"       to engine,
                "Gameplay"     to gameplay,
                "Story/Quests" to storyQuests
            )
            2 -> listOf(
                "Dialogues"    to dialogues,
                "Level Design" to levelDesign,
                "AI"           to ai
            )
            3 -> listOf(
                "World Design" to worldDesign,
                "Graphics"     to graphics,
                "Sound"        to sound
            )
            else -> emptyList()
        }
    }
}

@Immutable
data class TdBalance(
    val sliderName: String,
    val stage: Int,
    val designPercent: Int,
    val techPercent: Int
)