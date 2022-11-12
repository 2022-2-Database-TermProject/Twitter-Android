package com.database_termproject.twitter.ui.main.search;


import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.navigation.NavDirections;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.databinding.FragmentSearchBeforeBinding;
import com.database_termproject.twitter.ui.BaseFragment;

public class SearchBeforeFragment extends BaseFragment<FragmentSearchBeforeBinding> {
    @Override
    protected FragmentSearchBeforeBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return  FragmentSearchBeforeBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        binding.searchEt.requestFocus();
        setMyClickListener();
    }

    private void setMyClickListener(){
        // 취소 클릭 시,
        binding.searchCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController().popBackStack();
            }
        });

        // 검색 클릭 시,
        binding.searchEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if( findNavController().getCurrentDestination().getId() == R.id.searchBeforeFragment){
                        String search_word = binding.searchEt.getText().toString();
                        NavDirections action = SearchBeforeFragmentDirections.actionSearchBeforeFragmentToSearchAfterFragment(search_word);
                        findNavController().navigate(action);
                    }
                }

                return false;
            }
        });
    }
}
