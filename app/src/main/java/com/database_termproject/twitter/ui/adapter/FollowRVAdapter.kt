package com.database_termproject.twitter.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.database_termproject.twitter.data.User
import com.database_termproject.twitter.databinding.ItemFollowBinding

class FollowRVAdapter(val context: Context): RecyclerView.Adapter<FollowRVAdapter.ViewHolder>() {
    private val userList = arrayListOf<User>()

    inner class ViewHolder(val binding: ItemFollowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.itemFollowIdTv.text = user.user_id
            binding.itemFollowProfileTv.text = user.nickname

            Glide.with(context)
                .load(user.image)
                .apply(RequestOptions().circleCrop())
                .into(binding.itemFollowProfileIv)

            when(user.following_state){
                0 -> { // 기본 -> 팔로잉 show
                    binding.itemFollowFollowTv.visibility = View.VISIBLE
                    binding.itemFollowUnfollowTv.visibility = View.GONE
                    binding.itemFollowWaitTv.visibility = View.GONE
                }
                1 -> { // 팔로잉 -> 언팔로우 show
                    binding.itemFollowFollowTv.visibility = View.GONE
                    binding.itemFollowUnfollowTv.visibility = View.VISIBLE
                    binding.itemFollowWaitTv.visibility = View.GONE
                }
                2 -> { // 대기 -> 대기 중 show
                    binding.itemFollowFollowTv.visibility = View.GONE
                    binding.itemFollowUnfollowTv.visibility = View.GONE
                    binding.itemFollowWaitTv.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFollowBinding =
            ItemFollowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
        holder.binding.root.setOnClickListener {
            myItemClickListener.onClick(userList[position])
        }

        holder.binding.itemFollowFollowTv.setOnClickListener {
            myItemClickListener.onFollow(userList[position])
        }

        holder.binding.itemFollowUnfollowTv.setOnClickListener {
            myItemClickListener.onUnfollow(userList[position])
        }

        holder.binding.itemFollowWaitTv.setOnClickListener {
            myItemClickListener.onUnWait(userList[position])
        }
    }

    override fun getItemCount(): Int = userList.size

    /* Item 조작 */
    fun updateUser(list: ArrayList<User>){
        userList.clear();
        userList.addAll(list);
        notifyDataSetChanged()
    }

    /* 클릭 이벤트 */
    interface MyItemClickListener {
        fun onClick(user: User)
        fun onFollow(user: User)
        fun onUnfollow(user: User)
        fun onUnWait(user: User)
    }

    private lateinit var myItemClickListener: MyItemClickListener
    fun setMyClickListener(myItemClickListener: MyItemClickListener){
        this.myItemClickListener = myItemClickListener
    }
}