package com.database_termproject.twitter.ui.main.mypage;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.databinding.FragmentHomeBinding;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.ui.post.PostActivity;
import com.google.android.material.tabs.TabLayout;

public class MypageFragment extends AppCompatActivity {

    Fragment fragment1, fragment2, fragment3, fragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1 = new MypageFragment1();
        fragment2 = new MypageFragment2();
        fragment3 = new MypageFragment3();
        fragment4 = new MypageFragment4();

        getSupportFragmentManager().beginTransaction().add(R.id.mypage_detail, fragment1).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0){

                    selected = fragment1;

                }else if (position == 1){

                    selected = fragment2;

                }else if (position == 2){

                    selected = fragment3;

                }else if (position == 3){

                    selected = fragment4;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.mypage_detail, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}
