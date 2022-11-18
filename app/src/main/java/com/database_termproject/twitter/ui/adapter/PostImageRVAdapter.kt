package com.database_termproject.twitter.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.database_termproject.twitter.databinding.ItemAlbumBinding

class PostImageRVAdapter(val context: Context, private val imageList: ArrayList<String>) : RecyclerView.Adapter<PostImageRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding =
            ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size


    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String){
            Glide.with(context)
                .load(image)
                .fitCenter()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                .into(binding.itemAlbumIv)

            binding.itemAlbumDeleteIv.visibility = View.GONE
        }
    }
}