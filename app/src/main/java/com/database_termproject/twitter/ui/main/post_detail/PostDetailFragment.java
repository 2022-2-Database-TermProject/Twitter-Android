package com.database_termproject.twitter.ui.main.post_detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.database_termproject.twitter.databinding.FragmentPostDetailBinding;
import com.database_termproject.twitter.ui.BaseFragment;

public class PostDetailFragment extends BaseFragment<FragmentPostDetailBinding> {
    @Override
    protected FragmentPostDetailBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentPostDetailBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {

    }
}
