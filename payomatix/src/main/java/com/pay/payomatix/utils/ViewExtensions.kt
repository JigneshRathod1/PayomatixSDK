package com.pay.payomatix.utils

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.Group

/**
 * Set visibility of the view
 */
fun View.visible() {
    visibility = VISIBLE
}

fun View.hide() {
    visibility = INVISIBLE
}

fun View.gone() {
    visibility = GONE
}

fun View.isVisible(): Boolean {
    return visibility == VISIBLE
}

//-------View Visible Gone on Boolean Condition-------//
//-------Ex: expandable listview (In - Features of AppDetail, SubCategory Module,..., Compare App Screen (Bottom Info)-------//
fun View.visibleIfOrGone(isShown: Boolean) {
    if (isShown) {
        visible()
    } else {
        gone()
    }
}

fun View.visibleIfOrHide(isShown: Boolean) {
    if (isShown) {
        visible()
    } else {
        hide()
    }
}

infix fun View.onClick(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

//-------View gone Visible on Boolean Condition-------//
fun View.goneIfOrVisible(isGone: Boolean) {
    if (isGone) {
        gone()
    } else {
        visible()
    }
}

//-------if need to visibleView on boolean condition-------//
fun View.visibleIf(isVisible: Boolean) {
    if (isVisible) {
        visible()
    }
}

//-------if need to Hide(Gone)View on boolean condition-------//
fun View.goneIf(isGone: Boolean) {
    if (isGone) {
        gone()
    }
}

infix fun Group.onSafeGroupClick (onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(safeClickListener)
    }
}

infix fun Group.onNoSafeGroupClick (onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(defaultInterval = 0) {
        onSafeClick(it)
    }
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(safeClickListener)
    }
}

infix fun View.onSafeClick(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

infix fun View.onNoSafeClick(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(defaultInterval = 0) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun View.showKeyboard() {
    val imm: InputMethodManager? =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

class Size(var width: Int = 0, var height: Int = 0)

/**
 * User This Extension For Handle Multiple Click..
 */
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

