package com.database_termproject.twitter.ui.main.mypage;


import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.database_termproject.twitter.R;
import com.database_termproject.twitter.ui.adapter.MypageVPAdapter;
import com.database_termproject.twitter.ui.edit.EditActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;

public class MypageFragment extends Fragment {
    View view;
    MypageVPAdapter mypageVPAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, container, false);

        initVP();
        view.findViewById(R.id.change_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), EditActivity.class));
            }
        });

        return view;
    }


    private void initVP(){
        mypageVPAdapter = new MypageVPAdapter(this);

        ViewPager2 viewPager2 = view.findViewById(R.id.mypage_vp);
        viewPager2.setAdapter(mypageVPAdapter);


        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("트윗");
        tabs.add("리트윗");
        tabs.add("좋아요");

        TabLayout tabLayout = view.findViewById(R.id.mypage_tablayout);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
               tab.setText(tabs.get(position));
            }
        }).attach();
    }
}
