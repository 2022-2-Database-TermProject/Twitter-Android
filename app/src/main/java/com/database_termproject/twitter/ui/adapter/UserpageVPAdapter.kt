package com.database_termproject.twitter.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.database_termproject.twitter.ui.main.mypage.MypageRetweetFragment
import com.database_termproject.twitter.ui.main.mypage.MypageLikeFragment
import com.database_termproject.twitter.ui.main.mypage.MypageTweetFragment

class UserpageVPAdapter(fragment: Fragment, private val fragmentList: ArrayList<Fragment>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> fragmentList[0];
            1 -> fragmentList[1];
            else -> fragmentList[2];
        }
    }
}