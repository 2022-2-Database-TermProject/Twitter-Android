package com.database_termproject.twitter.ui.main.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavDirections;
import com.database_termproject.twitter.databinding.FragmentSearchBinding;
import com.database_termproject.twitter.ui.BaseFragment;

import java.util.Locale;

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
        // 검색 클릭 시,
        binding.searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchWord = binding.searchEt.getText().toString();
                NavDirections action = SearchFragmentDirections.actionSearchFragmentToSearchAfterFragment(searchWord);
                findNavController().navigate(action);
            }
        });
    }
}
