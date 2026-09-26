@file:Suppress("SpellCheckingInspection")

package com.naufal.gdtcalculator.data.repository

import com.naufal.gdtcalculator.data.model.MultiGenreDev
import com.naufal.gdtcalculator.data.model.Platform
import com.naufal.gdtcalculator.data.model.SingleGenreDev
import com.naufal.gdtcalculator.data.model.TdBalance
import com.naufal.gdtcalculator.data.model.Topic

object GDTRepository : InterfaceGDTRepository {

    private val topics: List<Topic>                  = listOf(
        Topic("Abstract",          "++",  "+++", "---", "---", "+",   "---", "+",   "++",  "+++"),
        Topic("Airplane",          "+++", "---", "+",   "+++", "+++", "+++", "+++", "+++", "++"),
        Topic("Aliens",            "+++", "+",   "+++", "---", "++",  "--",  "++",  "+++", "+++"),
        Topic("Alternate History", "+++", "+",   "+++", "+",   "++",  "---", "--",  "+++", "+++"),
        Topic("Assassin",          "+++", "--",  "+++", "+",   "---", "---", "--",  "+",   "+++"),
        Topic("Business",          "---", "+",   "+",   "+++", "+++", "---", "++",  "+++", "--"),
        Topic("City",              "--",  "--",  "--",  "+++", "+++", "--",  "++",  "+++", "+"),
        Topic("Colonization",      "--",  "--",  "---", "+++", "+++", "---", "--",  "+++", "+"),
        Topic("Comedy",            "---", "+++", "+",   "---", "---", "+++", "+",   "++",  "+++"),
        Topic("Construction",      "--",  "---", "---", "+++", "++",  "+",   "+",   "+++", "++"),
        Topic("Cooking",           "++",  "--",  "+",   "+++", "--",  "+++", "+",   "+++", "--"),
        Topic("Crime",             "+++", "--",  "+",   "++",  "--",  "---", "--",  "+",   "+++"),
        Topic("Cyberpunk",         "+++", "+",   "+++", "+",   "--",  "---", "--",  "++",  "+++"),
        Topic("Dance",             "--",  "---", "---", "+++", "---", "+++", "+++", "++",  "+"),
        Topic("Detective",         "---", "+++", "+++", "+",   "---", "++",  "++",  "+++", "+"),
        Topic("Disasters",         "++",  "+",   "--",  "+++", "+++", "--",  "--",  "++",  "+++"),
        Topic("Dungeon",           "+++", "+",   "+++", "+++", "+++", "---", "+",   "+++", "+++"),
        Topic("Dystopian",         "+",   "++",  "+",   "+++", "++",  "---", "--",  "+",   "+++"),
        Topic("Evolution",         "--",  "---", "---", "+++", "+++", "---", "+",   "+++", "--"),
        Topic("Expedition",        "--",  "++",  "--",  "++",  "+++", "---", "+",   "+++", "+"),
        Topic("Extreme Sports",    "+++", "---", "---", "+++", "--",  "++",  "+++", "--",  "+++"),
        Topic("Farming",           "---", "--",  "---", "+++", "---", "+++", "++",  "+++", "+"),
        Topic("Fantasy",           "+++", "+++", "+++", "+",   "+++", "---", "+++", "+++", "+++"),
        Topic("Fashion",           "---", "+",   "+++", "+++", "---", "+++", "+++", "++",  "--"),
        Topic("Game Dev",          "---", "--",  "---", "+++", "---", "+",   "++",  "+++", "--"),
        Topic("Government",        "---", "--",  "---", "---", "+++", "--",  "--",  "+++", "+"),
        Topic("Hacking",           "--",  "+",   "--",  "+++", "+++", "---", "--",  "++",  "+++"),
        Topic("History",           "+",   "+",   "+",   "+++", "+++", "++",  "+",   "+++", "++"),
        Topic("Horror",            "+++", "+++", "+",   "---", "--",  "+",   "--",  "++",  "+++"),
        Topic("Hospital",          "---", "---", "+",   "+++", "+",   "--",  "--",  "+++", "+"),
        Topic("Hunting",           "+++", "++",  "++",  "+++", "--",  "++",  "++",  "+++", "++"),
        Topic("Law",               "---", "+++", "++",  "++",  "++",  "---", "+",   "+++", "--"),
        Topic("Life",              "---", "+++", "++",  "+++", "---", "+",   "+++", "+++", "+"),
        Topic("Mad Science",       "++",  "+++", "--",  "++",  "---", "---", "+",   "++",  "+++"),
        Topic("Martial Arts",      "+++", "+",   "+++", "+++", "--",  "+++", "--",  "++",  "+++"),
        Topic("Medieval",          "+++", "+++", "+++", "+",   "+++", "--",  "+++", "+++", "++"),
        Topic("Military",          "+++", "---", "+",   "+++", "+++", "---", "--",  "++",  "+++"),
        Topic("Movies",            "+",   "+",   "---", "+++", "---", "+++", "++",  "+++", "++"),
        Topic("Music",             "+++", "++",  "---", "+++", "---", "+++", "+++", "++",  "+"),
        Topic("Mystery",           "---", "+++", "+++", "+",   "---", "+",   "+",   "++",  "+++"),
        Topic("Mythology",         "+++", "+",   "++",  "++",  "+",   "--",  "--",  "+++", "+++"),
        Topic("Ninja",             "+++", "+",   "+",   "---", "+",   "++",  "+++", "++",  "++"),
        Topic("Pirate",            "+",   "+++", "++",  "++",  "--",  "+",   "+++", "+++", "+"),
        Topic("Post Apocalyptic",  "+++", "+",   "+++", "---", "++",  "---", "--",  "++",  "+++"),
        Topic("Prison",            "+++", "+++", "+",   "+++", "+",   "---", "--",  "++",  "+++"),
        Topic("Racing",            "++",  "---", "+",   "+++", "--",  "+++", "+++", "+++", "++"),
        Topic("Rhythm",            "+++", "--",  "--",  "+++", "---", "+++", "+++", "++",  "+"),
        Topic("Romance",           "---", "+++", "+",   "++",  "---", "++",  "+",   "+++", "+++"),
        Topic("School",            "+",   "+++", "+++", "+++", "+++", "+",   "+++", "++",  "--"),
        Topic("Sci-Fi",            "+++", "+++", "+++", "+++", "+++", "+",   "+",   "+++", "+++"),
        Topic("Space",             "+++", "+",   "---", "+++", "+++", "--",  "+",   "+++", "+++"),
        Topic("Sports",            "+++", "---", "---", "+++", "--",  "+++", "+++", "+++", "+"),
        Topic("Spy",               "+++", "+++", "+++", "+",   "--",  "+",   "+",   "++",  "+++"),
        Topic("Superheroes",       "+++", "---", "++",  "---", "---", "--",  "+++", "+++", "+++"),
        Topic("Surgery",           "+",   "--",  "---", "+++", "--",  "---", "+",   "+++", "++"),
        Topic("Technology",        "---", "--",  "---", "+++", "++",  "---", "+",   "+++", "++"),
        Topic("Thief",             "++",  "+",   "+++", "++",  "---", "++",  "--",  "+++", "+++"),
        Topic("Time Travel",       "++",  "+++", "+++", "--",  "---", "--",  "++",  "+++", "+"),
        Topic("Transport",         "---", "---", "---", "+++", "+++", "---", "++",  "+++", "--"),
        Topic("UFO",               "+++", "+",   "---", "+",   "+++", "+",   "+",   "+++", "++"),
        Topic("Vampire",           "+++", "+",   "+++", "--",  "---", "--",  "--",  "+++", "+++"),
        Topic("Virtual Pet",       "---", "+",   "++",  "+++", "++",  "+++", "+++", "++",  "--"),
        Topic("Vocabulary",        "--",  "--",  "--",  "+++", "+++", "+++", "++",  "+++", "--"),
        Topic("Werewolf",          "+++", "+",   "+++", "---", "---", "--",  "--",  "++",  "+++"),
        Topic("Wild West",         "++",  "--",  "+++", "---", "---", "--",  "+++", "++",  "+++"),
        Topic("Zombies",           "+++", "--",  "---", "---", "++",  "+++", "++",  "+",   "+++")
    )
    private val genres: List<String>                 = listOf("Action", "Adventure", "RPG", "Simulation", "Strategy", "Casual")
    private val platforms: List<Platform>            = listOf(
        Platform("PC",               "++",  "+++", "++",  "+++", "+++", "---", "+",   "++",  "+++"),
        Platform("G64",              "++",  "+++", "++",  "++",  "+++", "--",  "+",   "++",  "+++"),
        Platform("TES",              "+",   "--",  "+",   "+",   "--",  "+++", "+++", "++",  "---"),
        Platform("Master V",         "++",  "--",  "+",   "+",   "--",  "+++", "++",  "+++", "--"),
        Platform("Gameling",         "+",   "--",  "++",  "++",  "---", "+++", "+++", "++",  "---"),
        Platform("Vena Gear",        "++",  "+",   "+",   "++",  "---", "+++", "++",  "+++", "+"),
        Platform("Vena Oasis",       "+++", "+",   "+",   "++",  "---", "--",  "+",   "+++", "++"),
        Platform("Super TES",        "++",  "++",  "++",  "+++", "--",  "++",  "+++", "++",  "--"),
        Platform("Playsystem",       "+++", "+",   "+++", "++",  "--",  "---", "+",   "+++", "++"),
        Platform("TES 64",           "++",  "+",   "--",  "+",   "--",  "++",  "+++", "++",  "++"),
        Platform("DreamVast",        "+++", "--",  "+",   "+++", "--",  "--",  "--",  "+++", "+++"),
        Platform("Playsystem 2",     "+++", "+",   "+++", "++",  "--",  "++",  "++",  "+++", "+"),
        Platform("mBox",             "+++", "+",   "++",  "++",  "--",  "--",  "+",   "+++", "++"),
        Platform("Game Sphere",      "+",   "+",   "--",  "+",   "--",  "+++", "++",  "++",  "+"),
        Platform("GS",               "++",  "++",  "+++", "++",  "++",  "+++", "+++", "++",  "+"),
        Platform("PPS",              "+++", "--",  "+++", "+",   "+",   "+",   "+",   "++",  "+++"),
        Platform("mBox 360",         "+++", "++",  "+++", "++",  "--",  "++",  "+",   "++",  "+++"),
        Platform("Nuu",              "+",   "---", "--",  "+++", "--",  "+++", "+++", "+++", "--"),
        Platform("Playsystem 3",     "+++", "++",  "++",  "+++", "--",  "+",   "+",   "+++", "++"),
        Platform("grPhone",          "+",   "+",   "--",  "++",  "--",  "+++", "++",  "+++", "---"),
        Platform("grPad",            "+",   "++",  "--",  "++",  "++",  "+++", "++",  "+++", "---"),
        Platform("mPad",             "--",  "++",  "+",   "++",  "--",  "++",  "--",  "++",  "+"),
        Platform("Wuu",              "++",  "--",  "+",   "+++", "--",  "+++", "++",  "+++", "--"),
        Platform("OYA",              "++",  "--",  "+",   "++",  "+",   "+++", "+",   "+++", "++"),
        Platform("mBox One",         "+++", "+",   "++",  "++",  "--",  "++",  "--",  "+++", "+"),
        Platform("Playsystem 4",     "+++", "+",   "+++", "++",  "--",  "++",  "+",   "+++", "++"),
        Platform("mBox Next",        "++",  "++",  "++",  "+",   "--",  "+++", "++",  "+++", "+"),
        Platform("Playsystem 5",     "+++", "--",  "++",  "+++", "--",  "++",  "+",   "+++", "++"),
        Platform("Custom Console",   "???", "???", "???", "???", "???", "???", "???", "???", "???")
    )
    private val singleGenreDev: List<SingleGenreDev> = listOf(
        SingleGenreDev("Action",     1.8,  "+++", "++",  "--",  "---", "++",  "+++", "~",   "+++", "++"),
        SingleGenreDev("Adventure",  0.4,  "--",  "~",   "+++", "+++", "~",   "--",  "+++", "++",  "~"),
        SingleGenreDev("RPG",        0.6,  "--",  "++",  "+++", "+++", "++",  "~",   "+++", "++",  "~"),
        SingleGenreDev("Simulation", 1.6,  "++",  "+++", "~",   "--",  "++",  "+++", "~",   "+++", "++"),
        SingleGenreDev("Strategy",   1.4,  "++",  "+++", "~",   "--",  "+++", "++",  "+++", "~",   "++"),
        SingleGenreDev("Casual",     0.5,  "---", "+++", "--",  "--",  "+++", "---", "--",  "+++", "++")
    )
    private val multiGenreDev: List<MultiGenreDev>   = listOf(
        MultiGenreDev("Action-Adventure",   1.33, "++",  "~",   "~",   "--",  "~",   "++",  "~",   "++",  "~"),
        MultiGenreDev("Action-RPG",         1.4,  "++",  "++",  "~",   "--",  "++",  "++",  "~",   "++",  "~"),
        MultiGenreDev("Action-Simulation",  1.73, "++",  "++",  "--",  "---", "++",  "+++", "~",   "+++", "++"),
        MultiGenreDev("Action-Strategy",    1.67, "++",  "++",  "--",  "---", "++",  "++",  "~",   "++",  "++"),
        MultiGenreDev("Action-Casual",      1.37, "~",   "++",  "--",  "---", "++",  "~",   "--",  "+++", "++"),
        MultiGenreDev("Adventure-Action",   0.87, "~",   "~",   "++",  "~",   "~",   "~",   "++",  "++",  "~"),
        MultiGenreDev("Adventure-RPG",      0.47, "--",  "~",   "+++", "+++", "~",   "--",  "+++", "++",  "~"),
        MultiGenreDev("Adventure-Simulation",0.8, "--",  "~",   "++",  "++",  "~",   "~",   "++",  "++",  "~"),
        MultiGenreDev("Adventure-Strategy", 0.73, "--",  "~",   "++",  "++",  "~",   "--",  "+++", "~",   "~"),
        MultiGenreDev("Adventure-Casual",   0.43, "---", "~",   "++",  "++",  "~",   "---", "++",  "++",  "~"),
        MultiGenreDev("RPG-Action",         1.0,  "~",   "++",  "++",  "~",   "++",  "~",   "++",  "++",  "~"),
        MultiGenreDev("RPG-Adventure",      0.53, "--",  "~",   "+++", "+++", "~",   "--",  "+++", "++",  "~"),
        MultiGenreDev("RPG-Simulation",     0.93, "--",  "++",  "++",  "++",  "++",  "~",   "++",  "++",  "~"),
        MultiGenreDev("RPG-Strategy",       0.87, "--",  "++",  "++",  "++",  "++",  "~",   "+++", "~",   "~"),
        MultiGenreDev("RPG-Casual",         0.57, "---", "++",  "++",  "++",  "++",  "--",  "++",  "++",  "~"),
        MultiGenreDev("Simulation-Action",  1.67, "++",  "++",  "--",  "--",  "++",  "+++", "~",   "+++", "++"),
        MultiGenreDev("Simulation-Adventure",1.2, "~",   "++",  "~",   "~",   "~",   "++",  "~",   "++",  "~"),
        MultiGenreDev("Simulation-RPG",     1.27, "~",   "++",  "~",   "~",   "++",  "++",  "~",   "++",  "~"),
        MultiGenreDev("Simulation-Strategy",1.53, "++",  "+++", "~",   "--",  "++",  "++",  "~",   "++",  "++"),
        MultiGenreDev("Simulation-Casual",  1.23, "~",   "+++", "--",  "--",  "++",  "~",   "--",  "+++", "++"),
        MultiGenreDev("Strategy-Action",    1.53, "++",  "++",  "--",  "---", "++",  "++",  "++",  "~",   "++"),
        MultiGenreDev("Strategy-Adventure", 1.07, "~",   "++",  "~",   "~",   "++",  "~",   "+++", "~",   "~"),
        MultiGenreDev("Strategy-RPG",       1.13, "~",   "++",  "~",   "~",   "++",  "~",   "+++", "~",   "~"),
        MultiGenreDev("Strategy-Simulation",1.47, "++",  "+++", "~",   "--",  "++",  "++",  "++",  "~",   "++"),
        MultiGenreDev("Strategy-Casual",    1.1,  "~",   "+++", "--",  "--",  "+++", "~",   "++",  "~",   "++"),
        MultiGenreDev("Casual-Action",      0.93, "--",  "++",  "--",  "---", "++",  "--",  "--",  "+++", "++"),
        MultiGenreDev("Casual-Adventure",   0.47, "---", "++",  "~",   "~",   "++",  "---", "~",   "++",  "~"),
        MultiGenreDev("Casual-RPG",         0.53, "---", "++",  "~",   "~",   "++",  "---", "~",   "++",  "~"),
        MultiGenreDev("Casual-Simulation",  0.87, "--",  "+++", "--",  "--",  "++",  "--",  "--",  "+++", "++"),
        MultiGenreDev("Casual-Strategy",    0.8,  "--",  "+++", "--",  "--",  "+++", "--",  "~",   "++",  "++")
    )
    private val tdBalance: List<TdBalance>           = listOf(
        TdBalance("Engine",       1, 20, 80),
        TdBalance("Gameplay",     1, 80, 20),
        TdBalance("Story/Quests", 1, 80, 20),
        TdBalance("Dialogues",    2, 90, 10),
        TdBalance("Level Design", 2, 40, 60),
        TdBalance("AI",           2, 20, 80),
        TdBalance("World Design", 3, 60, 40),
        TdBalance("Graphics",     3, 50, 50),
        TdBalance("Sound",        3, 60, 40)
    )

    override fun getTopics()     = topics
    override fun getGenres()    = genres
    override fun getPlatforms()  = platforms

    override fun getSingleGenreDev(genre: String): SingleGenreDev?{
        return singleGenreDev.find {it.genre.equals(genre, ignoreCase = true)}
    }
    override fun getMultiGenreDev(primary: String, secondary: String): MultiGenreDev?{
        val combo = "$primary-$secondary"
        return multiGenreDev.find {it.genreCombination.equals(combo, ignoreCase = true)}
    }
    override fun getTdBalance(stage: Int): List<TdBalance> {
        return tdBalance.filter {it.stage == stage}
    }
}