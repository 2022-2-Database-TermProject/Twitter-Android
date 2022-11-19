package com.database_termproject.twitter.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.database_termproject.twitter.ui.main.mypage.MypageRetweetFragment
import com.database_termproject.twitter.ui.main.mypage.MypageLikeFragment
import com.database_termproject.twitter.ui.main.mypage.MypageTweetFragment

class MypageVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MypageTweetFragment()
            1 -> MypageRetweetFragment()
            else -> MypageLikeFragment()
        }
    }
}