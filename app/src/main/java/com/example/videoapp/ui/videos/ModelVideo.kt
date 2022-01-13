package com.example.videoapp.ui.videos

class ModelVideo {

    var postId : String? = null
    var postVideo : String? = null
    var publisher : String? = null
    var title : String? = null

    constructor()

    constructor(postId: String?, postVideo: String?, publisher: String?,title: String) {
        this.postId = postId
        this.postVideo = postVideo
        this.publisher = publisher
        this.title = title
    }


}