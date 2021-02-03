package com.moskofidi.fintech.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.moskofidi.fintech.DevApplication
import com.moskofidi.fintech.api.Responce
import com.moskofidi.fintech.api.WebService
import com.moskofidi.fintech.mvp.views.TopView
import com.moskofidi.fintech.repository.TopRepository

@InjectViewState
class TopPresenter(private val mRepository: TopRepository) : MvpPresenter<TopView>() {

    private val webService = WebService(mRepository.pageData, mRepository.publicationData)

    fun loadGif() {
        if (DevApplication.getNetworkState()) {
            viewState.showContent()
            viewState.isEnablePrevBtn()

            when (webService.getGif(mRepository.pageData, mRepository.publicationData)) {
                Responce.SUCCESS -> {
                    viewState.setGif(mRepository.publicationData.gifURL)
                    viewState.setText(mRepository.publicationData.description)
                }
                Responce.NO_CONNECTION -> {
                    viewState.hideProgress()
                    viewState.hideContent()
                    viewState.showNetworkError()
                }
                Responce.NO_DATA -> {
                    viewState.hideProgress()
                    viewState.hideContent()
                    viewState.showDataError()
                }
            }

            viewState.hideSwipeRefresh()
        } else {
            viewState.hideProgress()
            viewState.hideContent()
            viewState.showNetworkError()
            viewState.hideSwipeRefresh()
        }
    }
}