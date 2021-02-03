package com.moskofidi.fintech.repository

import com.moskofidi.fintech.models.PageData
import com.moskofidi.fintech.models.PublicationData

class LatestRepository {
    private val topic = "latest"
    var pageData = PageData(0, topic, 0)
    lateinit var publicationData: PublicationData
}