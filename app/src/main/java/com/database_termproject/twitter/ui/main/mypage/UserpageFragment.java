package com.database_termproject.twitter.ui.main.mypage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.databinding.FragmentUserpageBinding;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.ui.adapter.MypageVPAdapter;
import com.database_termproject.twitter.ui.adapter.UserpageVPAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class UserpageFragment extends BaseFragment<FragmentUserpageBinding> {
    String args;

    UserpageVPAdapter userpageVPAdapter;

    @Override
    protected FragmentUserpageBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentUserpageBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        args = UserpageFragmentArgs.fromBundle(getArguments()).getUserId();
        initVP();
        setMyClickListener();

        showToast(args);
    }

    private void initVP(){
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new UserpageTweetFragment(args));
        fragmentList.add(new UserpageRetweetFragment(args));
        fragmentList.add(new UserpageLikeFragment(args));

        userpageVPAdapter = new UserpageVPAdapter(this, fragmentList);
        binding.userpageVp.setAdapter(userpageVPAdapter);

        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("트윗");
        tabs.add("리트윗");
        tabs.add("좋아요");

        new TabLayoutMediator(binding.userpageTablayout, binding.userpageVp, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabs.get(position));
            }
        }).attach();
    }

    private void setMyClickListener(){
        // Follow 클릭 시,
        binding.userpageFollowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Unfollow 클릭 시,
        binding.userpageUnfollowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Unwait 클릭 시,
        binding.userpageWaitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    // TODO: 유저 정보 조회 JDBC
    // TODO: 팔로우 JDBC
    // TODO: 언팔로우 JDBC
    // TODO: 팔로우 대기 취소 JDBC
}
