package com.moskofidi.fintech.mvp.views

import com.arellomobile.mvp.MvpView

interface LatestView : MvpView {

    fun showProgress()
    fun hideProgress()

    fun showNetworkError()
    fun showDataError()
    fun hideError()

    fun showContent()
    fun hideContent()

    fun setGif(url: String)
    fun clearGif()

    fun setText(description: String)
    fun cleanText()

    fun showSwipeRefresh()
    fun hideSwipeRefresh()

    fun isEnablePrevBtn()
}