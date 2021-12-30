package com.example.videoapp.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.core.view.View

class PostAdapter( private  var postList : MutableList<PostClass>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PostAdapter.PostViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class PostViewHolder(v : android.view.View) : RecyclerView.ViewHolder(v),
        android.view.View.OnClickListener{

        private  var view: android.view.View = v
        private lateinit var postClass : PostClass

        override fun onClick(p0: android.view.View?) {
            TODO("Not yet implemented")
        }

            init {
                v.setOnClickListener(this)
            }


    }
}