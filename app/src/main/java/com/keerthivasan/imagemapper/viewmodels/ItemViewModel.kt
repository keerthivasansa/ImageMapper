package com.keerthivasan.imagemapper.viewmodels

import androidx.lifecycle.ViewModel
import com.keerthivasan.imagemapper.screens.item.Item
import com.keerthivasan.imagemapper.Seller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ItemState(val activityName: String, val currentItem: Item?, val sellers: List<Seller>)

class ItemViewModel : ViewModel() {
    private var _uiState = MutableStateFlow(
        ItemState(
            activityName = "Create Item",
            currentItem = null,
            sellers = listOf()
        )
    )
    val state = _uiState.value
    val uiState = _uiState.asStateFlow()

    private fun setState(data: ItemState) {
        _uiState.value = data
    }

    fun setActivityName(name: String) {
        setState(_uiState.value.copy(activityName = name))
    }

    fun setSellers(sellers: List<Seller>) {
        setState(_uiState.value.copy(sellers = sellers))
    }

    fun setCurrentItem(currentItem: Item?) {
        setState(_uiState.value.copy(currentItem = currentItem))
    }

    fun setItemDescription(description: String) {
        val newItem = _uiState.value.currentItem?.copy(description = description)
        setCurrentItem(newItem)
    }

    fun setItemSeller(sellerId: String) {
        val newItem = _uiState.value.currentItem?.copy(sellerId = sellerId)
        setCurrentItem(newItem)
    }

}