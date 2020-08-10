package com.derekyu.testapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.derekyu.testapp.R
import com.derekyu.testapp.data.model.MyLoadState
import kotlinx.android.synthetic.main.view_my_state.view.*

class MyStateView<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_my_state, this)

        state_button.setOnClickListener {
            onRetryCallback?.invoke()
        }
    }

    var dataView: View? = null
    var onRetryCallback: (() -> Unit)? = null
    private lateinit var state: MyLoadState<T>

    @SuppressLint("SetTextI18n")
    fun setState(state: MyLoadState<T>, isEmptyData: Boolean) {
        this.state = state
        when (state) {
            is MyLoadState.Success -> {
                if (isEmptyData) {
                    state_button.text = context.getString(R.string.app_page_empty)
                    this.visibility = View.VISIBLE
                    state_button.visibility = View.VISIBLE
                    progress_bar.visibility = View.GONE
                    dataView?.visibility = View.INVISIBLE
                } else {
                    this.visibility = View.GONE
                    state_button.visibility = View.GONE
                    progress_bar.visibility = View.GONE
                    dataView?.visibility = View.VISIBLE
                }
            }
            is MyLoadState.Fail -> {
                state_button.text =
                    "${context.getString(R.string.retry)} (${state.error.errorMsg(context)})"

                this.visibility = View.VISIBLE
                state_button.visibility = View.VISIBLE
                progress_bar.visibility = View.GONE
                dataView?.visibility = View.INVISIBLE
            }
            is MyLoadState.Loading -> {
                this.visibility = View.VISIBLE
                state_button.visibility = View.GONE
                progress_bar.visibility = View.VISIBLE
                dataView?.visibility = View.INVISIBLE
            }
        }
    }
}