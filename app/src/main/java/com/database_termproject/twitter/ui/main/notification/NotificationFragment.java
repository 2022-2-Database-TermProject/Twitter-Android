package com.database_termproject.twitter.ui.main.notification;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.database_termproject.twitter.databinding.FragmentNotificationBinding;
import com.database_termproject.twitter.ui.BaseFragment;

public class NotificationFragment extends BaseFragment<FragmentNotificationBinding> {
    @Override
    protected FragmentNotificationBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentNotificationBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {

    }
}
