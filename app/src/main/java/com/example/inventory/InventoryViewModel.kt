package com.example.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val itemDao: ItemDao
) : ViewModel() {

    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        if (isEntryValid(itemName, itemPrice, itemCount)) {
            val item = getNewItemEntry(itemName, itemPrice, itemCount)
            insertItem(item)
        }
    }

    private fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isEmpty() || itemPrice.isEmpty() || itemCount.isEmpty()) {
            return false
        }

        return true
    }

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }
}

class InventoryViewModelFactory(
    private val itemDao: ItemDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}