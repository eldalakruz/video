package com.example.videoapp.ui.videos

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.videoapp.R

class AdapterVideo(private var context: Context, private var videoArrayList: ArrayList<ModelVideo>?
) : RecyclerView.Adapter<AdapterVideo.HolderVideo>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderVideo {

        val view =LayoutInflater.from(context).inflate(R.layout.row_video, parent, false)
        return HolderVideo(view)
    }

    override fun onBindViewHolder(holder: HolderVideo, position: Int) {

        val modelVideo =videoArrayList!![position]
        val videoUrl = modelVideo.postVideo
        setVideoUrl(modelVideo, holder)

    }

    private fun setVideoUrl(modelVideo: ModelVideo, holder: HolderVideo) {

        holder.progressBar.visibility = View.VISIBLE

        val videoUrl : String? = modelVideo.postVideo
        val mediaController = MediaController(context)
        mediaController.setAnchorView(holder.videoView)
        val videoUri = Uri.parse(videoUrl)

        holder.videoView.setMediaController(mediaController)
        holder.videoView.setVideoURI(videoUri)
        holder.videoView.requestFocus()

        holder.videoView.setOnPreparedListener {mediaPlayer ->
            mediaPlayer.start()
        }
        holder.videoView.setOnInfoListener(MediaPlayer.OnInfoListener{mp, what, extra ->

            when(what){
                MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START ->{
                    holder.progressBar.visibility = View.VISIBLE
                    return@OnInfoListener true
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_START ->{
                    holder.progressBar.visibility = View.VISIBLE
                    return@OnInfoListener true
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_END ->{
                    holder.progressBar.visibility = View.GONE
                    return@OnInfoListener true
                }
            }


            false
        })
        holder.videoView.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.start()
        }
    }

    override fun getItemCount(): Int {

        return videoArrayList!!.size
    }

    class HolderVideo(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var videoView : VideoView = itemView.findViewById(R.id.videos_view)
        var progressBar : ProgressBar = itemView.findViewById(R.id.videoProgressBar)
    }
}