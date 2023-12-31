package com.onurcancansu.socialmediaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.common.collect.ComparisonChain.start
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.onurcancansu.socialmediaapp.R
import com.onurcancansu.socialmediaapp.databinding.VideoItemBinding
import com.onurcancansu.socialmediaapp.model.UserModel
import com.onurcancansu.socialmediaapp.model.VideoModel

class VideoListAdapter(
    options: FirestoreRecyclerOptions<VideoModel>
) : FirestoreRecyclerAdapter<VideoModel, VideoListAdapter.VideoViewHolder>(options)
 {
    inner class VideoViewHolder(private val binding : VideoItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindVideo(videoModel: VideoModel){
            Firebase.firestore.collection("users")
                .document(videoModel.uploaderId)
                .get().addOnSuccessListener {
                    val userModel = it?.toObject(UserModel::class.java)
                    userModel?.apply {
                        binding.usernameView.text = username
                        //bind profilepic
                        Glide.with(binding.profileIcon).load(profilePic)
                            .circleCrop()
                            .apply(
                                RequestOptions().placeholder(R.drawable.icon_person)
                            )
                            .into(binding.profileIcon)
                    }
                }
            binding.captionView.text = videoModel.title
            binding.progressBar.visibility = View.VISIBLE
            binding.videoView.apply {
                setVideoPath(videoModel.url)
                setOnPreparedListener {
                    binding.progressBar.visibility = View.GONE
                    it.start()
                    it.isLooping = true
                }
                setOnClickListener {
                    if(isPlaying){
                        pause()
                        binding.pauseIcon.visibility = View.VISIBLE
                    }else{
                        start()
                        binding.pauseIcon.visibility = View.GONE
                    }
                }
            }

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = VideoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoViewHolder(binding)
    }
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int, model: VideoModel) {
        holder.bindVideo(model)
    }
}
