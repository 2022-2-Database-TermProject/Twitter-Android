package com.database_termproject.twitter.ui.main.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.database_termproject.twitter.databinding.FragmentSearchAfterBinding;
import com.database_termproject.twitter.ui.BaseFragment;

public class SearchAfterFragment extends BaseFragment<FragmentSearchAfterBinding> {
    @Override
    protected FragmentSearchAfterBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSearchAfterBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {

    }
}
