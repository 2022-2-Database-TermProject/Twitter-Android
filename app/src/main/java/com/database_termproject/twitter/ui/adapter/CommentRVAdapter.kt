package com.database_termproject.twitter.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.database_termproject.twitter.data.Comment
import com.database_termproject.twitter.data.Post
import com.database_termproject.twitter.databinding.ItemPostBinding
import com.database_termproject.twitter.ui.adapter.PostImageRVAdapter

class CommentRVAdapter(val context: Context) : RecyclerView.Adapter<CommentRVAdapter.ViewHolder>() {
    private val commentList = arrayListOf<Comment>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPostBinding =
            ItemPostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commentList[position])

        holder.binding.itemPostLikeLayout.setOnClickListener {
            myItemClickListener.like(commentList[position])
        }
    }

    override fun getItemCount(): Int = commentList.size

    inner class ViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment){
            binding.itemPostProfileTv.text = comment.nickname
            binding.itemPostUseridTv.text = "@" + comment.writer_id
            binding.itemPostContentTv.text = comment.content

            if(comment.num_of_likes > 0){
                binding.itemPostLikeTv.text = comment.num_of_likes.toString()
            }else{
                binding.itemPostLikeTv.text = ""
            }
            binding.itemPostRetweetLayout.visibility = View.GONE

//            if(post.writer_id == getUserId()){
//                binding.itemPostMoreIv.visibility = View.VISIBLE
//            }else{
//                binding.itemPostMoreIv.visibility = View.GONE
//            }
        }
    }

    /* Item 조작 */
    fun addComments(list: ArrayList<Comment>){
        commentList.clear();
        commentList.addAll(list);
        notifyDataSetChanged()
    }

    /* 클릭 이벤트 */
    interface MyItemClickListener {
        fun like(comment: Comment)
    }

    private lateinit var myItemClickListener: MyItemClickListener
    fun setMyClickListener(myItemClickListener: MyItemClickListener){
        this.myItemClickListener = myItemClickListener
    }

}