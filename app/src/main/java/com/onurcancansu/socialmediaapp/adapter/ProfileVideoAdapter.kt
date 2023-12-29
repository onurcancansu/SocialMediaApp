package com.onurcancansu.socialmediaapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.VideoView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.onurcancansu.socialmediaapp.SingleVideoPlayerActivity
import com.onurcancansu.socialmediaapp.databinding.ProfileVideoItemBinding
import com.onurcancansu.socialmediaapp.model.VideoModel

class ProfileVideoAdapter(options: FirestoreRecyclerOptions<VideoModel>)
    :FirestoreRecyclerAdapter<VideoModel, VideoListAdapter.VideoViewHolder>(options)
{

    inner class VideoViewHolder(private val binding : ProfileVideoItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(video : VideoModel){
            Glide.with(binding.thumbnailImageView)
                .load(video.url)
                .into(binding.thumbnailImageView)
            binding.thumbnailImageView.setOnClickListener{
                val intent = Intent(binding.thumbnailImageView.context,SingleVideoPlayerActivity::class.java)
                intent.putExtra("videoId", video.videoId)
                binding.thumbnailImageView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListAdapter.VideoViewHolder {
        val binding = ProfileVideoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: VideoListAdapter.VideoViewHolder,
        position: Int,
        model: VideoModel
    ) {
        holder.bindVideo(videoModel = VideoModel())

    }


}