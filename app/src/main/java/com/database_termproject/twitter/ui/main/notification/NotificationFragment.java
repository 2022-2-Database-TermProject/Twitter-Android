package com.database_termproject.twitter.ui.main.notification;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.database_termproject.twitter.databinding.FragmentNotificationBinding;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.ui.edit.EditActivity;

public class NotificationFragment extends BaseFragment<FragmentNotificationBinding> {
    @Override
    protected FragmentNotificationBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentNotificationBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        binding.notificationMoreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), EditActivity.class));
            }
        });
    }
}
