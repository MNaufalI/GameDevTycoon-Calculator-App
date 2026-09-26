package com.naufal.gdtcalculator.ui.base

interface InterfaceSelectable<T> {
    fun onItemSelected(item: T)
    fun clearSelection()
}