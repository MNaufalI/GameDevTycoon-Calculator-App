package com.naufal.gdtcalculator.ui.development

import com.naufal.gdtcalculator.data.model.DevData
import com.naufal.gdtcalculator.data.model.MultiGenreDev
import com.naufal.gdtcalculator.data.model.SingleGenreDev
import com.naufal.gdtcalculator.data.model.TdBalance
import com.naufal.gdtcalculator.data.repository.GDTRepository
import com.naufal.gdtcalculator.ui.base.BaseViewModel

class DevViewModel : BaseViewModel() {

    override fun clearSelection() {
        clearSearch()
    }

    fun getSingleDevData(genre: String): SingleGenreDev? {
        return GDTRepository.getSingleGenreDev(genre)
    }

    fun getMultiDevData(primaryGenre: String, secondaryGenre: String): MultiGenreDev? {
        return GDTRepository.getMultiGenreDev(primaryGenre, secondaryGenre)
    }

    fun getTdBalanceForStage(stage: Int): List<TdBalance> {
        return GDTRepository.getTdBalance(stage)
    }

    fun getSlidersForStage(devData: DevData, stage: Int): List<Pair<String, String>> {
        return devData.getSlidersForStage(stage)
    }
}