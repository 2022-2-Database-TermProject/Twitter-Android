package com.database_termproject.twitter.ui.main.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.database_termproject.twitter.R;

public class UserpageTweetFragment extends Fragment {

    public String user_id;
    public UserpageTweetFragment(String user_id){
        this.user_id = user_id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userpage_tweet, container, false);

        return view;
    }
}
