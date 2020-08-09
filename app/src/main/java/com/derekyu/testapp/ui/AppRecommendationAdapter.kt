package com.derekyu.testapp.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.derekyu.testapp.data.model.AppInfoDTO

class AppRecommendationAdapter() : RecyclerView.Adapter<AppRecommendationItemViewHolder>() {
    private val list: MutableList<AppInfoDTO> = arrayListOf()

    override fun onBindViewHolder(holder: AppRecommendationItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppRecommendationItemViewHolder {
        return AppRecommendationItemViewHolder.create(parent)
    }

    override fun getItemCount(): Int = list.size

    fun submitData(list: List<AppInfoDTO>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}
