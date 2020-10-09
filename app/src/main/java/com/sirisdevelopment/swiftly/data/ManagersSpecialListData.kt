package com.sirisdevelopment.swiftly.data

import com.sirisdevelopment.swiftly.data.objects.SwiftlyManagerSpecialItemData

interface ManagersSpecialListData {
    var canvasUnit: Int

    var managerSpecialItemDataList: List<SwiftlyManagerSpecialItemData>

    fun size(): Int

    fun get(position: Int): List<SwiftlyManagerSpecialItemData>?
}
