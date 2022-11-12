package com.database_termproject.twitter.ui.main.search;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.database_termproject.twitter.databinding.FragmentSearchAfterBinding;
import com.database_termproject.twitter.ui.BaseFragment;

public class SearchAfterFragment extends BaseFragment<FragmentSearchAfterBinding> {


    @Override
    protected FragmentSearchAfterBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSearchAfterBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        String args = SearchAfterFragmentArgs.fromBundle(getArguments()).getSearchWord();
        binding.searchAfterTv.setText(args);

        Log.d("search001", "현재 위치 - " + findNavController().getCurrentDestination());
        setMyClickListener();
    }

    private void setMyClickListener(){
        binding.searchAfterBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController().popBackStack();
            }
        });
    }
}
