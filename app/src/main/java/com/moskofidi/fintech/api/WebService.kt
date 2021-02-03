package com.moskofidi.fintech.api

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.moskofidi.fintech.DevApplication
import com.moskofidi.fintech.models.PageData
import com.moskofidi.fintech.models.PublicationData
import com.moskofidi.fintech.models.ResultList
import com.moskofidi.fintech.models.gson

enum class Responce {
    NO_DATA,
    NO_CONNECTION,
    SUCCESS
}

class WebService(pageData: PageData, publicationData: PublicationData) {

    fun getGif(pageData: PageData, publicationData: PublicationData) : Responce {
        var jsonIn = ""
        val url = "https://developerslife.ru/${pageData.topic}/${pageData.page}?json=true"
        lateinit var responce: Responce

        url.httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Success -> {
                    jsonIn = result.get()
                    val publicationList = gson.fromJson(jsonIn, ResultList::class.java)
                    if (publicationList.result.isEmpty()) {
                        responce = Responce.NO_DATA
                    } else {
                        responce = Responce.SUCCESS
                        publicationData.gifURL = publicationList.result[pageData.postsCount].gifURL
                        publicationData.description = publicationList.result[pageData.postsCount].description
                    }
                }
                is Result.Failure -> {
                    if (DevApplication.getNetworkState()) {
                        responce = Responce.NO_DATA
                    } else {
                        responce = Responce.NO_CONNECTION
                    }
                }
            }
        }
        return responce
    }
}