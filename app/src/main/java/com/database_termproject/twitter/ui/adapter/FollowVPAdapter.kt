package com.database_termproject.twitter.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.database_termproject.twitter.ui.main.follow.FollowFollowerFragment
import com.database_termproject.twitter.ui.main.follow.FollowFollowingFragment

class FollowVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FollowFollowerFragment()
            else -> FollowFollowingFragment()
        }
    }

}
