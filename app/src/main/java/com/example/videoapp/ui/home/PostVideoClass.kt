package com.example.videoapp.ui.home

class PostVideoClass {

    private var postId : String =""
    private var postVideo : String = ""
    private var publisher : String =""

    constructor()

    constructor(postId : String, postVideo : String, publisher : String){

        this.postId =  postId
        this.postVideo = postVideo
        this.publisher = publisher
    }

    fun getPostId() : String{

        return postId
    }

    fun getPostVideo() : String{

        return postVideo
    }

    fun getPublisher() : String{

        return publisher
    }

}