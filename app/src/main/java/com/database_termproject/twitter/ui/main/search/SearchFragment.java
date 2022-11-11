package com.database_termproject.twitter.ui.main.search;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.databinding.FragmentSearchBinding;
import com.database_termproject.twitter.ui.BaseFragment;

public class SearchFragment extends BaseFragment<FragmentSearchBinding> {
    @Override
    protected FragmentSearchBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSearchBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        setMyClickListener();
    }

    private void setMyClickListener(){
        Log.d("search0", "현재 위치 - " + findNavController().getCurrentDestination());

        // 검색 입력 텍스트뷰 클릭 시,
        binding.searchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("search1", "현재 위치 - " + findNavController().getCurrentDestination());
                findNavController().navigate(R.id.action_searchFragment_to_searchBeforeFragment);
                Log.d("search2", "현재 위치 - " + findNavController().getCurrentDestination());
            }
        });
    }
}
