package com.moskofidi.fintech.mvp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.moskofidi.fintech.R
import com.moskofidi.fintech.mvp.presenters.LatestPresenter
import com.moskofidi.fintech.mvp.views.LatestView
import com.moskofidi.fintech.repository.LatestRepository
import kotlinx.android.synthetic.main.fragment_latest.*

class LatestFragment : MvpFragment(), LatestView {

    private val mRepository = LatestRepository()

    @InjectPresenter
    lateinit var mPresenter: LatestPresenter

    @ProvidePresenter
    fun provideLatestPresenter(): LatestPresenter {
        return LatestPresenter(mRepository = mRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_latest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresenter.loadGif()

        swipeRefreshLatest.setOnRefreshListener {
            showSwipeRefresh()
            clearGif()
            cleanText()
            showProgress()

            mPresenter.loadGif()
        }

        swipeRefreshLatest.setColorSchemeResources(
            android.R.color.black
        )

        btnPrevLatest.setOnClickListener {
            clearGif()
            cleanText()
            showProgress()

            mRepository.pageData.prevPost()
            if (mRepository.pageData.postsCount > 0) {
                mPresenter.loadGif()
            } else {
                if (mRepository.pageData.postsCount == 0) {
                    if (mRepository.pageData.page == 0) {
                        mPresenter.loadGif()
                        btnPrevLatest.isEnabled = false
                    } else {
                        mPresenter.loadGif()
                    }
                } else {
                    mRepository.pageData.prevPost()
                    mRepository.pageData.postsCount = 4
                    mPresenter.loadGif()
                }
            }
        }

        btnNextLatest.setOnClickListener {
            clearGif()
            cleanText()
            showProgress()

            mRepository.pageData.nextPost()
            isEnablePrevBtn()
            if (mRepository.pageData.postsCount <= 4) {
                mPresenter.loadGif()
            } else {
                mRepository.pageData.postsCount = 0
                mRepository.pageData.nextPage()
                mPresenter.loadGif()
            }
        }
    }

    override fun showProgress() {
        progressBarLatest.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBarLatest.visibility = View.INVISIBLE
    }

    override fun showNetworkError() {
        Glide.with(this).load(R.mipmap.baseline_wifi_off_black_48)
            .into(imgErrorLatest)
        textErrorLatest.text = resources.getString(R.string.noConnection)
        textErrorLatest.visibility = View.VISIBLE
        imgErrorLatest.visibility = View.VISIBLE
    }

    override fun showDataError() {
        Glide.with(this).load(R.mipmap.baseline_image_not_supported_black_48)
            .into(imgErrorLatest)
        textErrorLatest.text = resources.getString(R.string.noData)
        textErrorLatest.visibility = View.VISIBLE
        imgErrorLatest.visibility = View.VISIBLE
    }

    override fun hideError() {
        textErrorLatest.visibility = View.INVISIBLE
        imgErrorLatest.visibility = View.INVISIBLE
    }

    override fun showContent() {
        gifContainerLatest.visibility = View.VISIBLE

        btnNextLatest.visibility = View.VISIBLE
        btnPrevLatest.visibility = View.VISIBLE
    }

    override fun hideContent() {
        gifContainerLatest.visibility = View.INVISIBLE

        btnNextLatest.visibility = View.INVISIBLE
        btnPrevLatest.visibility = View.INVISIBLE
    }

    override fun setGif(url: String) {
//        requireActivity().runOnUiThread {
        Glide.with(this).asGif()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(GifRequestListener())
            .into(object : SimpleTarget<GifDrawable>() {
                override fun onResourceReady(
                    resource: GifDrawable,
                    transition: Transition<in GifDrawable>?
                ) {
                    imgViewLatest.setImageDrawable(resource)
                    resource.start()
                }
            })
//        }
    }

    override fun clearGif() {
        imgViewLatest.setImageDrawable(null)
    }

    override fun setText(description: String) {
        textViewLatest.text = description
    }

    override fun cleanText() {
        textErrorLatest.text = ""
    }

    override fun showSwipeRefresh() {
        swipeRefreshLatest.isRefreshing = true
    }

    override fun hideSwipeRefresh() {
        swipeRefreshLatest.isRefreshing = false
    }

    override fun isEnablePrevBtn() {
        btnPrevLatest.isEnabled =
            !(mRepository.pageData.postsCount == 0 && mRepository.pageData.page == 0)
    }

    inner class GifRequestListener() : RequestListener<GifDrawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<GifDrawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: GifDrawable?,
            model: Any?,
            target: Target<GifDrawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            hideProgress()
            return false
        }
    }
}