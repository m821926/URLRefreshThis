package com.pj.urlrefreshthis

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup.MarginLayoutParams
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MovableFloatingActionButton : FloatingActionButton, OnTouchListener {
    private var downRawX = 0f
    private var downRawY = 0f
    private var dX = 0f
    private var dY = 0f
    public var posX = 0f
    public var posY = 0f

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
    }

    fun setPosition(valX:Float,valY:Float){

//        // get device dimensions
//        val displayMetrics = DisplayMetrics()
//        var width = displayMetrics.widthPixels
//        var height = displayMetrics.heightPixels
//
//        val viewWidth = this.width
//        val viewHeight = this.height
//        val parentWidth = width
//        val parentHeight = height
//        val layoutParams = this.layoutParams as MarginLayoutParams
//
//        var newX = Math.max(
//            layoutParams.leftMargin.toFloat(),
//            valX
//        ) // Don't allow the FAB past the left hand side of the parent
//        newX = Math.min(
//            (parentWidth - viewWidth - layoutParams.rightMargin).toFloat(),
//            newX
//        ) // Don't allow the FAB past the right hand side of the parent
//
//        var newY = Math.max(
//            layoutParams.topMargin.toFloat(),
//            valY
//        ) // Don't allow the FAB past the top of the parent
//        newY = Math.min(
//            (parentHeight - viewHeight - layoutParams.bottomMargin).toFloat(),
//            newY
//        ) // Don't allow the FAB past the bottom of the parent
        this.animate()
            .x(valX)
            .y(valY)
            .setDuration(0)
            .start()
    }


    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val layoutParams = view.layoutParams as MarginLayoutParams
        val action = motionEvent.action
        return if (action == MotionEvent.ACTION_DOWN) {
            downRawX = motionEvent.rawX
            downRawY = motionEvent.rawY
            dX = view.x - downRawX
            dY = view.y - downRawY
            true // Consumed
        } else if (action == MotionEvent.ACTION_MOVE) {
            val viewWidth = view.width
            val viewHeight = view.height
            val viewParent = view.parent as View
            val parentWidth = viewParent.width
            val parentHeight = viewParent.height
            var newX = motionEvent.rawX + dX
            newX = Math.max(
                layoutParams.leftMargin.toFloat(),
                newX
            ) // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(
                (parentWidth - viewWidth - layoutParams.rightMargin).toFloat(),
                newX
            ) // Don't allow the FAB past the right hand side of the parent
            var newY = motionEvent.rawY + dY
            newY = Math.max(
                layoutParams.topMargin.toFloat(),
                newY
            ) // Don't allow the FAB past the top of the parent
            newY = Math.min(
                (parentHeight - viewHeight - layoutParams.bottomMargin).toFloat(),
                newY
            ) // Don't allow the FAB past the bottom of the parent

            //Save this position so that it can be consulted
            posX=newX
            posY=newY

            view.animate()
                .x(newX)
                .y(newY)
                .setDuration(0)
                .start()
            true // Consumed
        } else if (action == MotionEvent.ACTION_UP) {
            val upRawX = motionEvent.rawX
            val upRawY = motionEvent.rawY
            val upDX = upRawX - downRawX
            val upDY = upRawY - downRawY
            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                performClick()
            } else { // A drag
                true // Consumed
            }
        } else {
            super.onTouchEvent(motionEvent)
        }
    }

    companion object {
        private const val CLICK_DRAG_TOLERANCE =
            10f // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    }
}