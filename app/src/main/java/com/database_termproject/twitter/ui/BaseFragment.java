package com.database_termproject.twitter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment<F extends ViewBinding> extends Fragment {
    public F binding;
    protected abstract F getBinding(LayoutInflater inflater, ViewGroup container);

    void initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = getBinding(inflater, container);
    }

    protected abstract void initAfterBinding();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       this.initBinding(inflater, container);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initAfterBinding();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showToast(String message){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    public NavController findNavController(){
       return  NavHostFragment.findNavController(this);
    }
}
