package com.database_termproject.twitter.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.database_termproject.twitter.R
import com.database_termproject.twitter.databinding.ItemInterestBinding

class InterestRVAdapter(val context: Context): RecyclerView.Adapter<InterestRVAdapter.ViewHolder>() {
    val interests = arrayListOf<String>("Entertainment", "Food", "Study", "Sports", "Game");
    val has_interests = arrayListOf<String>();

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemInterestBinding =
            ItemInterestBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(interests[position])

        holder.binding.root.setOnClickListener {
            val this_interest = interests[position]
            if(has_interests.contains(this_interest)){ // Selected -> 해제
                has_interests.remove(this_interest)
                notifyDataSetChanged()
            }else{
                has_interests.add(this_interest)
                notifyDataSetChanged()
            }
            Log.d("RV", has_interests.toString());
        }
    }

    override fun getItemCount(): Int = interests.size;

    inner class ViewHolder(val binding: ItemInterestBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(interest: String){
            binding.itemInterestTv.text = interest

            if(has_interests.contains(interest)){ // Selected
                binding.root.setBackgroundResource(R.drawable.bg_check_selection)
                binding.itemInterestTv.setTextColor(ContextCompat.getColor(context, R.color.white))
            }else{

                binding.root.setBackgroundResource(R.drawable.bg_check_not_selection)
                binding.itemInterestTv.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    /* 아이템 관리 */
    fun addInterestList(list: ArrayList<String>){
        has_interests.clear()
        has_interests.addAll(list)
        notifyDataSetChanged()
    }

    fun getInterestList(): ArrayList<String>{
        return has_interests
    }
}