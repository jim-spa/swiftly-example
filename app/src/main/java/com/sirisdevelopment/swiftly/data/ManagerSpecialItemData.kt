package com.sirisdevelopment.swiftly.data

interface ManagerSpecialItemData {
    var displayName: String

    var height: Int

    var width: Int

    fun getOriginalPrice(): String

    fun getPrice(): String

    var imageUrl: String

    var position: Int
}
