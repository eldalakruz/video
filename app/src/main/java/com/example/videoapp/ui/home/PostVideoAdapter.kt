package com.example.videoapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.videoapp.R
import java.lang.System.*



class PostVideoAdapter(private val mContext : Context, mPost : List<PostVideoClass>)
    : RecyclerView.Adapter<PostVideoAdapter.PostVideoViewHolder>() {

    private var mPost : List<PostVideoClass>? = null

    init {
        this.mPost = mPost

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVideoViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.video_item_list, parent,
            false)
        return PostVideoViewHolder(view)
    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    override fun onBindViewHolder(holder: PostVideoViewHolder, position: Int) {


        val postVideoClass : PostVideoClass = mPost!![position]
        val videoUrl : String = postVideoClass.getPostVideo()

        setVideoUrl(postVideoClass, holder)

      /*  val  mediaController = MediaController(mContext)
        holder.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(holder.videoView)
        holder.videoView.setVideoURI(Uri.parse("$video"))
        holder.videoView.requestFocus()
        holder.videoView.setOnClickListener{
            holder.videoView.start()
        }*/



    }

    private fun setVideoUrl(
        postVideoClass: PostVideoClass,
        holder: PostVideoViewHolder
    ) {

        val videoUrl : String = postVideoClass.getPostVideo()
        val  mediaController = MediaController(mContext)
        mediaController.setAnchorView(holder.videoView)
        val videoUri = Uri.parse(videoUrl)

        holder.videoView.setMediaController(mediaController)
        holder.videoView.setVideoURI(videoUri)
        holder.videoView.requestFocus()

        holder.videoView.setOnPreparedListener {mediaPlayer ->
            mediaPlayer.start()
        }

        holder.videoView.setOnInfoListener(MediaPlayer.OnInfoListener{ mp, what, extra ->

            when(what){

                MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START ->{

                    return@OnInfoListener true
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_START ->{
                    return@OnInfoListener true
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_END ->{
                    return@OnInfoListener true
                }

            }


            false
        })


    }


    override fun getItemCount(): Int {
        return mPost!!.size
    }

     class PostVideoViewHolder(v: View) : RecyclerView.ViewHolder(v)
    {

        var videoView : VideoView = v.findViewById(R.id.videoItem)


    }



}