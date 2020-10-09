package com.sirisdevelopment.swiftly.data.objects

import com.google.gson.annotations.SerializedName
import com.sirisdevelopment.swiftly.data.ManagerSpecialItemData
import com.sirisdevelopment.swiftly.data.ManagersSpecialListData
import timber.log.Timber

class SwiftlyGrocerManagersSpecialDataPacket: ManagersSpecialListData {
    private var rowIndex = 0
    private var size = -1
    var swiftlyManagerSpecialItemDataMap = HashMap<Int, ArrayList<SwiftlyManagerSpecialItemData>>()

    @SerializedName("canvasUnit")
    override var canvasUnit = 0

    @SerializedName("managerSpecials")
    override var managerSpecialItemDataList: List<SwiftlyManagerSpecialItemData> = ArrayList()

    override fun size(): Int {
        if (size < 0) {
            var widthRemaining = canvasUnit
            rowIndex = 0
            Timber.d("size: adding: canvasUnit[%s] itemCount[%s]", canvasUnit, managerSpecialItemDataList.size)
            for (swiftlyManagerSpecialItemData in managerSpecialItemDataList) {
                if (widthRemaining >= swiftlyManagerSpecialItemData.width) {
                    Timber.d("size: adding: rowCount[%s] widthRemaining[%s] >= swiftlyManagerSpecialItemData.width[%s]",
                        rowIndex, widthRemaining, swiftlyManagerSpecialItemData.width)
                    widthRemaining -= swiftlyManagerSpecialItemData.width
                    swiftlyManagerSpecialItemData.position = rowIndex
                } else {
                    Timber.d("size: reset: rowCount[%s] widthRemaining[%s] >= swiftlyManagerSpecialItemData.width[%s]",
                        rowIndex, widthRemaining, swiftlyManagerSpecialItemData.width)
                    rowIndex++
                    widthRemaining = canvasUnit
                    widthRemaining -= swiftlyManagerSpecialItemData.width
                    swiftlyManagerSpecialItemData.position = rowIndex
                }
                Timber.d("size: completed: rowCount[%s] widthRemaining[%s] swiftlyManagerSpecialItemData.width[%s] item[%s]",
                    rowIndex, widthRemaining, swiftlyManagerSpecialItemData.width, swiftlyManagerSpecialItemData.displayName)
            }
            size = rowIndex + 1
        }
        return size
    }

    override fun get(position: Int): ArrayList<SwiftlyManagerSpecialItemData>? {
        if (size < 0) {
            size()
        }
        if (size == 0) {
            return null
        }
        if (swiftlyManagerSpecialItemDataMap.size < 1) {
            for (swiftlyManagerSpecialItemData in managerSpecialItemDataList) {
                if (swiftlyManagerSpecialItemDataMap.containsKey(swiftlyManagerSpecialItemData.position)) {
                    swiftlyManagerSpecialItemDataMap.get(swiftlyManagerSpecialItemData.position)?.add(swiftlyManagerSpecialItemData)
                } else {
                    val swiftlyManagerSpecialItemDataList = ArrayList<SwiftlyManagerSpecialItemData>()
                    swiftlyManagerSpecialItemDataList.add(swiftlyManagerSpecialItemData)
                    swiftlyManagerSpecialItemDataMap.put(swiftlyManagerSpecialItemData.position, swiftlyManagerSpecialItemDataList)
                }
            }
        }
        return swiftlyManagerSpecialItemDataMap.get(position)
    }
}

class SwiftlyManagerSpecialItemData: ManagerSpecialItemData {
    override var position = -1

    @SerializedName("display_name")
    override var displayName: String = ""

    @SerializedName("height")
    override var height = 0

    @SerializedName("width")
    override var width = 0

    @SerializedName("original_price")
    var originalPriceString: String = ""

    @SerializedName("price")
    var priceString: String = ""

    @SerializedName("imageUrl")
    override var imageUrl: String = ""

    var originalPriceInDollars = ""

    var priceInDollars = ""

    override fun getOriginalPrice(): String {
        if (originalPriceInDollars.length < 1) {
            try {
                originalPriceInDollars = "$$originalPriceString"
            } catch (e: NumberFormatException) {
                return "N/A"
            }
        }
        return originalPriceInDollars
    }

    override fun getPrice(): String {
        if (priceInDollars.length < 1) {
            try {
                priceInDollars = "$$priceString"
            } catch (e: NumberFormatException) {
                return "N/A"
            }
        }
        return priceInDollars
    }
}

