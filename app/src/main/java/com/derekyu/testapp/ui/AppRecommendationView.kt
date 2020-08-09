package com.derekyu.testapp.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.derekyu.testapp.R
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.ui.AppRecommendationAdapter
import kotlinx.android.synthetic.main.view_app_suggest_list.view.*

class AppRecommendationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    private val adapter: AppRecommendationAdapter
    init {
        LayoutInflater.from(context).inflate(R.layout.view_app_suggest_list, this)

        adapter = AppRecommendationAdapter()
        app_suggest_recycler_view.adapter = adapter
    }

    fun submitData(list: List<AppInfoDTO>) {
        adapter.submitData(list)
    }
}