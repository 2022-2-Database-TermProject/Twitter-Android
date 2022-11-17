package com.database_termproject.twitter.ui.post

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.database_termproject.twitter.databinding.ItemAlbumBinding
import com.database_termproject.twitter.utils.getRealPath

class PostAlbumRVAdapter(val context: Context) : RecyclerView.Adapter<PostAlbumRVAdapter.ViewHolder>() {
    private val imageList = arrayListOf<Uri>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding =
            ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageList[position])

        // 삭제 클릭 리스너
        holder.binding.itemAlbumDeleteIv.setOnClickListener {
            imageList.removeAt(position);
            notifyDataSetChanged();

            myItemClickListener.onDeleted(imageList);
        }
    }

    override fun getItemCount(): Int = imageList.size


    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Uri){
           val path = getRealPath(context, image)
            Glide.with(context)
                .load(path)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                .into(binding.itemAlbumIv)
        }
    }

    /* Item 조작 */
    fun addImages(list: ArrayList<Uri>){
        imageList.clear();
        imageList.addAll(list);
        notifyDataSetChanged()
    }

    fun addImage(img: Uri){
        imageList.add(img)
        notifyItemChanged(imageList.size-1)
    }

    fun getImages(): ArrayList<Uri>{
        return imageList
    }

    /* 클릭 이벤트 */
    interface MyItemClickListener {
        fun onDeleted(list: ArrayList<Uri>)
    }

    private lateinit var myItemClickListener: MyItemClickListener
    fun setMyClickListener(myItemClickListener: MyItemClickListener){
        this.myItemClickListener = myItemClickListener
    }

}