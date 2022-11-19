package com.database_termproject.twitter.ui.main.mypage;


import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.databinding.FragmentHomeBinding;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.ui.post.PostActivity;
import com.google.android.material.tabs.TabLayout;

public class UserpageFragment extends Fragment {

    Fragment fragment1, fragment2, fragment3;

    CheckBox followBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        fragment1 = new UserpageFragment1();
        fragment2 = new UserpageFragment2();
        fragment3 = new UserpageFragment3();

        followBtn = view.findViewById(R.id.user_follow);

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox)view).isChecked()) {
                    followBtn.setText("언팔로우");
                    followBtn.setTextColor(Color.BLACK);
                } else {
                    followBtn.setTextColor(Color.WHITE);
                }

            }
        });

        TabLayout tabs = view.findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {

                    selected = fragment1;

                } else if (position == 1) {

                    selected = fragment2;

                } else if (position == 2) {

                    selected = fragment3;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        return view;
    }
}
