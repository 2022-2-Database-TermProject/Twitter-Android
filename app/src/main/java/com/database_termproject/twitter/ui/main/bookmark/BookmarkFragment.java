package com.database_termproject.twitter.ui.main.bookmark;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.database_termproject.twitter.databinding.FragmentBookmarkBinding;
import com.database_termproject.twitter.ui.BaseFragment;

public class BookmarkFragment extends BaseFragment<FragmentBookmarkBinding> {
    @Override
    protected FragmentBookmarkBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentBookmarkBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {

    }
}
