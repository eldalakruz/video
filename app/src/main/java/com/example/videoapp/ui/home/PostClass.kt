package com.example.videoapp.ui.home

 class PostClass {

     private var postId : String = ""
     private var post : String = ""
     private var publisher : String = ""
     private  var title : String = ""

     constructor()

     constructor(postId : String, post : String, publisher : String){

         this.postId =  postId
         this.post = post
         this.publisher = publisher
         this.title = title
     }

     fun getPostId() : String{

         return postId
     }

     fun getPost() : String{

         return post
     }

     fun getPublisher() : String{

         return publisher
     }

     fun  getTitle() : String{

         return title
     }


}