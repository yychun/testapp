package com.derekyu.testapp.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.derekyu.testapp.data.model.AppInfoDTO

class AppRecommendationAdapter() : RecyclerView.Adapter<AppItemViewHolder>() {
    private val list: MutableList<AppInfoDTO> = arrayListOf()

    override fun onBindViewHolder(holder: AppItemViewHolder, position: Int) {
        holder.bind(position, list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemViewHolder {
        return AppItemViewHolder.create(parent)
    }

    override fun getItemCount(): Int = list.size

    fun submitData(list: List<AppInfoDTO>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}
