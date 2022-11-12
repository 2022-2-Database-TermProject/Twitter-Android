package com.database_termproject.twitter.ui.main.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.databinding.FragmentHomeBinding;
import com.database_termproject.twitter.ui.post.PostActivity;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    @Override
    protected FragmentHomeBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        // Click listener
        binding.homeNewpostIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), PostActivity.class));
            }
        });
    }
}
