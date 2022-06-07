package com.openclassrooms.realestatemanager.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView

/*
Fixed scroll bug Recyclerview with ViewPager
https://stackoverflow.com/questions/57243019/recyclerview-in-viewpager-wont-scroll
 */

class CustomRecyclerView : RecyclerView {
  constructor(@NonNull context: Context?) : super(context!!)
  constructor(@NonNull context: Context?, @Nullable attrs: AttributeSet?) : super(context!!, attrs)
  constructor(
    @NonNull context: Context?,
    @Nullable attrs: AttributeSet?,
    defStyleAttr: Int
  ) : super(context!!, attrs, defStyleAttr)

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    when (ev.action) {
      MotionEvent.ACTION_MOVE -> parent.requestDisallowInterceptTouchEvent(true)
    }
    return super.dispatchTouchEvent(ev)
  }
}