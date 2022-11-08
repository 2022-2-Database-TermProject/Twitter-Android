package com.database_termproject.twitter.ui.main.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.database_termproject.twitter.databinding.FragmentSearchBinding;
import com.database_termproject.twitter.ui.BaseFragment;

public class SearchFragment extends BaseFragment<FragmentSearchBinding> {
    @Override
    protected FragmentSearchBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSearchBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {

    }
}
