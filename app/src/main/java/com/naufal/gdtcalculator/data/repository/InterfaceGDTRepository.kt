package com.naufal.gdtcalculator.data.repository

import com.naufal.gdtcalculator.data.model.MultiGenreDev
import com.naufal.gdtcalculator.data.model.Platform
import com.naufal.gdtcalculator.data.model.SingleGenreDev
import com.naufal.gdtcalculator.data.model.TdBalance
import com.naufal.gdtcalculator.data.model.Topic

interface InterfaceGDTRepository {
    fun getTopics(): List<Topic>
    fun getPlatforms(): List<Platform>
    fun getGenres(): List<String>
    fun getSingleGenreDev(genre: String): SingleGenreDev?
    fun getMultiGenreDev(primary: String, secondary: String): MultiGenreDev?
    fun getTdBalance(stage: Int): List<TdBalance>
}