package com.example.videoapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.videoapp.R
import com.squareup.picasso.Picasso

class PostAdapter( private  val mContext :Context, mPost : List<PostClass> ) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>(){

    private var mPost : List<PostClass>? = null

    init {
        this.mPost = mPost
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.image_item_list, parent,
            false)
        return PostViewHolder(view)

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val postClass : PostClass = mPost!![position]
        Picasso.get().load(postClass.getPost()).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return mPost!!.size
    }

   inner class PostViewHolder(v : android.view.View) : RecyclerView.ViewHolder(v)
        {

        var imageView : ImageView



            init {
                imageView = v.findViewById(R.id.imageItem)
            }



    }
}