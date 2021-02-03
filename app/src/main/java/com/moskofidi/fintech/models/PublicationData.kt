package com.moskofidi.fintech.models

data class PublicationData(
//    val id: Int,
    var description: String,
    var gifURL: String,
//    val previewURL: String,
//    val videoURL: String,
//    val videoSize: Int,
//    val width: String,
//    val height: String
)

data class ResultList(var result: List<PublicationData>)
