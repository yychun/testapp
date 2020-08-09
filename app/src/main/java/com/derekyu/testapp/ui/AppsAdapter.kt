package com.derekyu.testapp.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.derekyu.testapp.data.model.AppInfoDTO

class AppsAdapter() : PagingDataAdapter<AppInfoDTO, AppItemViewHolder>(APP_COMPARATOR) {

    override fun onBindViewHolder(holder: AppItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(position, it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemViewHolder {
        return AppItemViewHolder.create(parent)
    }

    companion object {
        val APP_COMPARATOR = object : DiffUtil.ItemCallback<AppInfoDTO>() {
            override fun areContentsTheSame(
                oldItem: AppInfoDTO,
                newItem: AppInfoDTO
            ): Boolean = oldItem == newItem

            override fun areItemsTheSame(
                oldItem: AppInfoDTO,
                newItem: AppInfoDTO
            ): Boolean = oldItem.appID == newItem.appID
        }
    }
}
