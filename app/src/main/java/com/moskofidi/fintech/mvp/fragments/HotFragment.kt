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
import com.moskofidi.fintech.mvp.presenters.HotPresenter
import com.moskofidi.fintech.mvp.views.HotView
import com.moskofidi.fintech.repository.HotRepository
import kotlinx.android.synthetic.main.fragment_hot.*

class HotFragment : MvpFragment(), HotView {

    private val mRepository = HotRepository()

    @InjectPresenter
    lateinit var mPresenter: HotPresenter

    @ProvidePresenter
    fun provideLatestPresenter(): HotPresenter {
        return HotPresenter(mRepository = mRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresenter.loadGif()

        swipeRefreshHot.setOnRefreshListener {
            showSwipeRefresh()
            clearGif()
            cleanText()
            showProgress()

            mPresenter.loadGif()
        }

        swipeRefreshHot.setColorSchemeResources(
            android.R.color.black
        )

        btnPrevHot.setOnClickListener {
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
                        btnPrevHot.isEnabled = false
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

        btnNextHot.setOnClickListener {
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
        progressBarHot.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBarHot.visibility = View.INVISIBLE
    }

    override fun showNetworkError() {
        Glide.with(this).load(R.mipmap.baseline_wifi_off_black_48)
            .into(imgErrorHot)
        textErrorHot.text = resources.getString(R.string.noConnection)
        textErrorHot.visibility = View.VISIBLE
        imgErrorHot.visibility = View.VISIBLE
    }

    override fun showDataError() {
        Glide.with(this).load(R.mipmap.baseline_image_not_supported_black_48)
            .into(imgErrorHot)
        textErrorHot.text = resources.getString(R.string.noData)
        textErrorHot.visibility = View.VISIBLE
        imgErrorHot.visibility = View.VISIBLE
    }

    override fun hideError() {
        textErrorHot.visibility = View.INVISIBLE
        imgErrorHot.visibility = View.INVISIBLE
    }

    override fun showContent() {
        gifContainerHot.visibility = View.VISIBLE

        btnNextHot.visibility = View.VISIBLE
        btnPrevHot.visibility = View.VISIBLE
    }

    override fun hideContent() {
        gifContainerHot.visibility = View.INVISIBLE

        btnNextHot.visibility = View.INVISIBLE
        btnPrevHot.visibility = View.INVISIBLE
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
                    imgViewHot.setImageDrawable(resource)
                    resource.start()
                }
            })
//        }
    }

    override fun clearGif() {
        imgViewHot.setImageDrawable(null)
    }

    override fun setText(description: String) {
        textViewHot.text = description
    }

    override fun cleanText() {
        textErrorHot.text = ""
    }

    override fun showSwipeRefresh() {
        swipeRefreshHot.isRefreshing = true
    }

    override fun hideSwipeRefresh() {
        swipeRefreshHot.isRefreshing = false
    }

    override fun isEnablePrevBtn() {
        btnPrevHot.isEnabled =
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