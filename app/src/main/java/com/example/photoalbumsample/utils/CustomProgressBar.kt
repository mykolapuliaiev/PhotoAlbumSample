package com.example.photoalbumsample.utils

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import com.example.photoalbumsample.SampleApplication.Companion.appContext
import java.lang.ref.WeakReference

/**
 * Base class for displaying Progress Bar. Implemented as singleton.
 * For instantiating needs screen root layout or any other ViewGroup view.
 * Exists as long as showed, when progress not needed - instance cleared.
 */
class CustomProgressBar {
    companion object {
        fun getInstance(): CustomProgressBar {
            if (instance == null) {
                instance = CustomProgressBar()
            }
            return instance as CustomProgressBar
        }

        private var instance: CustomProgressBar? = null
    }

    private var progressLayout: WeakReference<RelativeLayout>? = null

    /**
     * Method for showing the progress bar. Takes ViewGroup view for basing a progress bar layout.
     *
     * @param layout Any {@link ViewGroup} inheritor as {@link ConstraintLayout}, {@link RelativeLayout}, {@link LinearLayout} etc.
     */
    fun showProgressBar(layout: ViewGroup) {
        if (progressLayout == null) {
            progressLayout = WeakReference(getProgressBarLayout())
            layout.addView(progressLayout?.get())
        }
        progressLayout?.get()?.visibility = View.VISIBLE
        progressLayout?.get()?.isFocusable = true
        progressLayout?.get()?.isClickable = true
    }

    /**
     * Method for hiding the progress bar. Also clears {@link CustomProgressBar} instance.
     */
    fun hideProgressBar() {
        progressLayout?.let {
            it.get()?.isFocusable = false
            it.get()?.isClickable = false
            it.get()?.visibility = View.GONE
            instance = null
        }
    }

    private fun getProgressBarLayout(): RelativeLayout {
        val progressBar = getProgressBar(appContext)
        val layoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).apply {
            addRule(RelativeLayout.CENTER_IN_PARENT)
        }

        return RelativeLayout(appContext).apply {
            gravity = Gravity.CENTER
            setLayoutParams(layoutParams)
            addView(progressBar)
            visibility = View.GONE
        }
    }

    private fun getProgressBar(context: Context): ProgressBar {
        val progressBarSize = getProgressBarSize(84)
        val layoutParams = RelativeLayout.LayoutParams(progressBarSize, progressBarSize)
        return ProgressBar(context).apply {
            id = ViewCompat.generateViewId()
            this.layoutParams = layoutParams
        }
    }

    private fun getProgressBarSize(px: Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        px.toFloat(),
        appContext.resources.displayMetrics
    ).toInt()
}