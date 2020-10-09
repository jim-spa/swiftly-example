package com.sirisdevelopment.swiftly.adapters

import android.graphics.Paint
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.sirisdevelopment.swiftly.R
import com.sirisdevelopment.swiftly.data.ManagerSpecialItemData
import com.sirisdevelopment.swiftly.data.ManagersSpecialListData
import com.sirisdevelopment.swiftly.data.SwiftlyRepository.Companion.gson
import com.sirisdevelopment.swiftly.util.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.manager_special_item.view.*
import kotlinx.android.synthetic.main.manager_special_item_row.view.*
import timber.log.Timber

class ManagerSpecialRecyclerAdapter(private var managersSpecialListData: ManagersSpecialListData, private var displayMetrics: DisplayMetrics):
    RecyclerView.Adapter<ManagerSpecialRecyclerAdapter.ManagerSpecialItemHolder>() {

    companion object {
        const val ITEMS_MARGIN = 12
    }
    var canvasUnits = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManagerSpecialItemHolder {
        Timber.d("onCreateViewHolder")
        val inflatedView = parent.inflate(R.layout.manager_special_item_row, false)
        val managerSpecialItemHolder = ManagerSpecialItemHolder(inflatedView)
        canvasUnits = managersSpecialListData.canvasUnit
        managerSpecialItemHolder.canvasWidth = canvasUnits
        managerSpecialItemHolder.pixelUnits = (displayMetrics.widthPixels / canvasUnits) - ITEMS_MARGIN
        return managerSpecialItemHolder
    }

    fun setManagersSpecialsListData(managersSpecialListData: ManagersSpecialListData) {
        this.managersSpecialListData = managersSpecialListData
        canvasUnits = managersSpecialListData.canvasUnit
        notifyDataSetChanged()
        Timber.d("setManagersSpecialsListData: managersSpecialListData list updated")
    }

    override fun getItemCount() = managersSpecialListData.size()

    override fun onBindViewHolder(
        holder: ManagerSpecialItemHolder,
        position: Int
    ) {
        val managerSpecialItemList = managersSpecialListData.get(position)
        if (managerSpecialItemList != null) {
            holder.bindManagerSpecialItem(managerSpecialItemList)
        }
        Timber.d("onBindViewHolder: view[%s]", gson.toJson(managerSpecialItemList))
    }

    class ManagerSpecialItemHolder(v: View): RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var managerSpecialItemDataList: List<ManagerSpecialItemData>? = null
        var canvasWidth = 0
        var pixelUnits = 0

        fun bindManagerSpecialItem(managerSpecialItemDataList: List<ManagerSpecialItemData>) {
            this.managerSpecialItemDataList = managerSpecialItemDataList
            val itemRow = view.managers_specials_items_linear_layout

            // TODO
            // this is inefficient - not actually recycling the views.. should be fixed
            // because it is causing the jank in the scrolling
            itemRow.removeAllViews()
            itemRow.weightSum = canvasWidth.toFloat()
            for (managerSpecialItemData in managerSpecialItemDataList) {
                val inflatedRowView = itemRow.inflate(R.layout.manager_special_item, false)
                inflatedRowView.setPadding(12, 2, 12, 2)
                inflatedRowView.layoutParams.height = pixelUnits * managerSpecialItemData.height
                inflatedRowView.layoutParams.width = pixelUnits * managerSpecialItemData.width
                Picasso.get().load(managerSpecialItemData.imageUrl).into(inflatedRowView.itemImage)
                inflatedRowView.itemDescription.text = managerSpecialItemData.displayName
                inflatedRowView.itemSpecialPrice.text = managerSpecialItemData.getPrice()
                if (!managerSpecialItemData.getPrice().equals(managerSpecialItemData.getOriginalPrice())) {
                    inflatedRowView.itemPrice.text = managerSpecialItemData.getOriginalPrice()
                    inflatedRowView.itemPrice.paintFlags =
                        inflatedRowView.itemPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    inflatedRowView.itemPrice.visibility = View.INVISIBLE
                    inflatedRowView.itemPrice.layoutParams.height = 0
                }
                itemRow.addView(inflatedRowView)
                Timber.d("bindManagerSpecialItem: view[%s]", managerSpecialItemData.displayName)
            }
        }
    }
}