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
import com.moskofidi.fintech.mvp.presenters.TopPresenter
import com.moskofidi.fintech.mvp.views.TopView
import com.moskofidi.fintech.repository.TopRepository
import kotlinx.android.synthetic.main.fragment_top.*

class TopFragment : MvpFragment(), TopView {

    private val mRepository = TopRepository()

    @InjectPresenter
    lateinit var mPresenter: TopPresenter

    @ProvidePresenter
    fun provideLatestPresenter(): TopPresenter {
        return TopPresenter(mRepository = mRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresenter.loadGif()

        swipeRefreshTop.setOnRefreshListener {
            showSwipeRefresh()
            clearGif()
            cleanText()
            showProgress()

            mPresenter.loadGif()
        }

        swipeRefreshTop.setColorSchemeResources(
            android.R.color.black
        )

        btnPrevTop.setOnClickListener {
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
                        btnPrevTop.isEnabled = false
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

        btnNextTop.setOnClickListener {
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
        progressBarTop.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBarTop.visibility = View.INVISIBLE
    }

    override fun showNetworkError() {
        Glide.with(this).load(R.mipmap.baseline_wifi_off_black_48)
            .into(imgErrorTop)
        textErrorTop.text = resources.getString(R.string.noConnection)
        textErrorTop.visibility = View.VISIBLE
        imgErrorTop.visibility = View.VISIBLE
    }

    override fun showDataError() {
        Glide.with(this).load(R.mipmap.baseline_image_not_supported_black_48)
            .into(imgErrorTop)
        textErrorTop.text = resources.getString(R.string.noData)
        textErrorTop.visibility = View.VISIBLE
        imgErrorTop.visibility = View.VISIBLE
    }

    override fun hideError() {
        textErrorTop.visibility = View.INVISIBLE
        imgErrorTop.visibility = View.INVISIBLE
    }

    override fun showContent() {
        gifContainerTop.visibility = View.VISIBLE

        btnNextTop.visibility = View.VISIBLE
        btnPrevTop.visibility = View.VISIBLE
    }

    override fun hideContent() {
        gifContainerTop.visibility = View.INVISIBLE

        btnNextTop.visibility = View.INVISIBLE
        btnPrevTop.visibility = View.INVISIBLE
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
                    imgViewTop.setImageDrawable(resource)
                    resource.start()
                }
            })
//        }
    }

    override fun clearGif() {
        imgViewTop.setImageDrawable(null)
    }

    override fun setText(description: String) {
        textViewTop.text = description
    }

    override fun cleanText() {
        textErrorTop.text = ""
    }

    override fun showSwipeRefresh() {
        swipeRefreshTop.isRefreshing = true
    }

    override fun hideSwipeRefresh() {
        swipeRefreshTop.isRefreshing = false
    }

    override fun isEnablePrevBtn() {
        btnPrevTop.isEnabled = !(mRepository.pageData.postsCount == 0 && mRepository.pageData.page == 0)
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