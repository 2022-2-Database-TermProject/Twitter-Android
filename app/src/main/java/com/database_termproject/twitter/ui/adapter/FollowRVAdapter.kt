package com.database_termproject.twitter.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.database_termproject.twitter.data.User
import com.database_termproject.twitter.databinding.ItemFollowBinding

class FollowRVAdapter: RecyclerView.Adapter<FollowRVAdapter.ViewHolder>() {
    private val userList = arrayListOf<User>()

    inner class ViewHolder(val binding: ItemFollowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.itemFollowIdTv.text = user.user_id
            binding.itemFollowProfileTv.text = user.nickname
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
    }

    private lateinit var myItemClickListener: MyItemClickListener
    fun setMyClickListener(myItemClickListener: MyItemClickListener){
        this.myItemClickListener = myItemClickListener
    }
}