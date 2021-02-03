package com.moskofidi.fintech.models

open class PageData(
    var postsCount: Int,
    var topic: String,
    var page: Int
) {
    fun nextPost() {
        this.postsCount++
    }

    fun prevPost() {
        this.postsCount--
    }

    fun nextPage() {
        this.page++
    }

    fun prevPage() {
        this.page--
    }
}