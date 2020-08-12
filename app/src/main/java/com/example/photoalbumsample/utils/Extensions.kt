package com.example.photoalbumsample.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.photoalbumsample.networking.ResultWrapper

/**
 * Returns drawable by given ID
 */
fun Context.getDrawableByID(id: Int): Drawable? = ContextCompat.getDrawable(this, id)

/**
 * Converts density to pixels
 */
fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

/**
 * Used to append action in any case of request execution
 *
 * @param doForAll Callback to invoke
 * @return
 */
fun <T> ResultWrapper<T>.doForAll(doForAll: () -> Unit): ResultWrapper<T> {
    doForAll()
    return this
}

/**
 * Used to append action in case of success response
 *
 * @param success Callback to invoke
 * @return
 */
fun <T> ResultWrapper<T>.isSuccess(success: (T) -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.Success) {
        success(this.value)
    }
    return this
}

/**
 * Used to append action in case of error response
 *
 * @param error Callback to invoke
 * @return
 */
fun <T> ResultWrapper<T>.isError(error: (code: Int?, message: String?) -> Unit): ResultWrapper<T> {
    when (this) {
        is ResultWrapper.GenericError -> error(this.code, this.message)
        is ResultWrapper.NetworkError -> error(null, null)
    }
    return this
}

/**
 * RecyclerView item click listeners
 */
inline fun RecyclerView.onItemClick(crossinline action: (position: Int) -> Unit) =
    setOnItemClickListener(onClick = action)

inline fun RecyclerView.onLongItemClick(crossinline action: (position: Int) -> Unit) =
    setOnItemClickListener(onLongClick = action)

inline fun RecyclerView.setOnItemClickListener(
    crossinline onClick: (position: Int) -> Unit = {},
    crossinline onLongClick: (position: Int) -> Unit = {}
) {

    addOnItemTouchListener(
        RecyclerItemClickListener(this,
            object : RecyclerItemClickListener.OnItemClickListener {
                override fun onLongItemClick(child: View, position: Int) {
                    onLongClick(position)
                }

                override fun onItemClick(view: View, position: Int) {
                    onClick(position)
                }
            })
    )
}