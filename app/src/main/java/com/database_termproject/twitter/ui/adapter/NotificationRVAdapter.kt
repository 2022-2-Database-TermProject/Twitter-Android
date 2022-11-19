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
import com.database_termproject.twitter.databinding.ItemNotificationBinding

class NotificationRVAdapter(val context: Context): RecyclerView.Adapter<NotificationRVAdapter.ViewHolder>() {
    private val userList = arrayListOf<User>()

    inner class ViewHolder(val binding: ItemNotificationBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.itemNotificationIdTv.text = user.user_id
            binding.itemNotificationNicknameTv.text = user.nickname

            Glide.with(context)
                .load(user.image)
                .apply(RequestOptions().circleCrop())
                .into(binding.itemNotificationProfileIv)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNotificationBinding =
            ItemNotificationBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])

        holder.binding.itemNotifictionConfirmIv.setOnClickListener {
            myItemClickListener.onConfirm(userList[position])
        }

        holder.binding.itemNotifictionRejectIv.setOnClickListener {
            myItemClickListener.onReject(userList[position])
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
        fun onConfirm(user: User)
        fun onReject(user: User)
    }

    private lateinit var myItemClickListener: MyItemClickListener
    fun setMyClickListener(myItemClickListener: MyItemClickListener){
        this.myItemClickListener = myItemClickListener
    }
}