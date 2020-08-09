package com.derekyu.testapp.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.derekyu.testapp.R
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.utils.dpToPx

class AppItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val number: TextView = view.findViewById(R.id.number)
    private val icon: ImageView = view.findViewById(R.id.icon)
    private val name: TextView = view.findViewById(R.id.name)
    private val category: TextView = view.findViewById(R.id.category)
    private val rating: AppRatingView = view.findViewById(R.id.rating)

    fun bind(position: Int, appInfoDTO: AppInfoDTO) {
        val ranking = (position + 1)
        number.text = ranking.toString()
        val isOdd = ranking % 2 == 1
        val transformer = if (isOdd) RoundedCorners(
            ROUND_CORNER_SIZE.dpToPx(itemView.context).toInt()
        ) else CircleCrop()
        appInfoDTO.artworkUrl?.let {
            Glide.with(itemView.context).load(it)
                .centerCrop()
                .override(ICON_SIZE.dpToPx(itemView.context).toInt())
                .transform(transformer)
                .into(icon)
        }
        name.text = appInfoDTO.name
        category.text = appInfoDTO.category
        if (appInfoDTO.hasRating) {
            rating.setRating(appInfoDTO.rating!!, appInfoDTO.ratingCount!!)
            rating.visibility = View.VISIBLE
        } else {
            rating.visibility = View.GONE
        }
    }

    companion object {
        const val ICON_SIZE = 100
        const val ROUND_CORNER_SIZE = 25

        fun create(parent: ViewGroup): AppItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_app_lookup, parent, false)
            return AppItemViewHolder(view)
        }
    }
}