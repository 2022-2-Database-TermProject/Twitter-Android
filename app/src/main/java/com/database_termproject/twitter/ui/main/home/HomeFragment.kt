package com.database_termproject.twitter.ui.main.home

import com.database_termproject.twitter.databinding.FragmentHomeBinding
import com.database_termproject.twitter.ui.BaseFragment


class HomeFragment: BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val DATABASE = "mydb"
    private val USER = "root"
    private val PASSWORD = "021019@wa"

    override fun initAfterBinding() {
        binding.homeBtn.setOnClickListener {
            val userId = binding.idEt.text
            val password = binding.pwEt.text


            val query =
                "select user_id from user where user_id = \"$userId\" and password=\"$password\""

        }
    }
}