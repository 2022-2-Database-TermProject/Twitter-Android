package com.database_termproject.twitter.ui.main.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.database_termproject.twitter.databinding.FragmentSearchBeforeBinding;
import com.database_termproject.twitter.ui.BaseFragment;

public class SearchBeforeFragment extends BaseFragment<FragmentSearchBeforeBinding> {
    @Override
    protected FragmentSearchBeforeBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return  FragmentSearchBeforeBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {

    }
}
