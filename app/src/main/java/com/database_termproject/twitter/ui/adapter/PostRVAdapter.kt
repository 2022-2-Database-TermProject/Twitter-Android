package com.database_termproject.twitter.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.database_termproject.twitter.data.Post
import com.database_termproject.twitter.databinding.ItemPostBinding

class PostRVAdapter(val context: Context) : RecyclerView.Adapter<PostRVAdapter.ViewHolder>() {
    private val postList = arrayListOf<Post>()
    private lateinit var homePostImageRVAdapter: PostImageRVAdapter

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPostBinding =
            ItemPostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position])

        holder.binding.root.setOnClickListener {
            myItemClickListener.onClick(postList[position])
        }

        holder.binding.itemPostLikeLayout.setOnClickListener {
            myItemClickListener.like(postList[position])
        }

        holder.binding.itemPostRetweetLayout.setOnClickListener {
            myItemClickListener.retweet(postList[position])
        }
    }

    override fun getItemCount(): Int = postList.size


    inner class ViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post){
            binding.itemPostProfileTv.text = post.nickname
            binding.itemPostUseridTv.text = "@" + post.writer_id
            binding.itemPostContentTv.text = post.content

            if(post.num_of_likes > 0) binding.itemPostLikeTv.text = post.num_of_likes.toString()
            else binding.itemPostLikeTv.text = ""
            if(post.retweet_num > 0) binding.itemPostRetweetTv.text = post.retweet_num.toString()
            else binding.itemPostRetweetTv.text = ""

            homePostImageRVAdapter = PostImageRVAdapter(context, post.fileList)
            binding.itemPostImagesRv.adapter = homePostImageRVAdapter

//            if(post.writer_id == getUserId()){
//                binding.itemPostMoreIv.visibility = View.VISIBLE
//            }else{
//                binding.itemPostMoreIv.visibility = View.GONE
//            }
        }
    }

    /* Item 조작 */
    fun addPosts(list: ArrayList<Post>){
        postList.clear();
        postList.addAll(list);
        notifyDataSetChanged()
    }

    /* 클릭 이벤트 */
    interface MyItemClickListener {
        fun onClick(post: Post)
        fun retweet(post: Post)
        fun like(post: Post)
    }

    private lateinit var myItemClickListener: MyItemClickListener
    fun setMyClickListener(myItemClickListener: MyItemClickListener){
        this.myItemClickListener = myItemClickListener
    }

}