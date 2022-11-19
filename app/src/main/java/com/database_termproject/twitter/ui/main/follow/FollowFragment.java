package com.database_termproject.twitter.ui.main.follow;

import static com.database_termproject.twitter.utils.SharedPreferenceManagerKt.getUserId;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.database_termproject.twitter.databinding.FragmentFollowBinding;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.ui.adapter.FollowVPAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import java.util.ArrayList;

public class FollowFragment extends BaseFragment<FragmentFollowBinding> {
    FollowVPAdapter followVPAdapter;

    @Override
    protected FragmentFollowBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentFollowBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        initVP();

        binding.followDetailNicknameTv.setText("@" + getUserId());
        binding.followDetailBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController().popBackStack();
            }
        });
    }

    private void initVP(){
        followVPAdapter = new FollowVPAdapter(this);
        binding.followViewpager.setAdapter(followVPAdapter);

        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("팔로워");
        tabs.add("팔로잉");

        new TabLayoutMediator(binding.followTablayout, binding.followViewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabs.get(position));
            }
        }).attach();
    }
}
