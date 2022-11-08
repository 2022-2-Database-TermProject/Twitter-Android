package com.database_termproject.twitter.ui.main.mypage;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.database_termproject.twitter.databinding.FragmentMypageBinding;
import com.database_termproject.twitter.ui.BaseFragment;

public class MypageFragment extends BaseFragment<FragmentMypageBinding> {

    @Override
    protected FragmentMypageBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMypageBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {

    }
}
