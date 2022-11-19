package com.database_termproject.twitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;


public abstract class BaseActivity<Binding extends ViewBinding> extends AppCompatActivity {

    public Binding binding;
    protected abstract Binding getBinding();

    private InputMethodManager  imm = null;

    void initBinding() {
        binding = getBinding();
    }

    protected abstract void initAfterBinding();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initBinding();
        setContentView(binding.getRoot());

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        transparentStatusBar();
        initAfterBinding();
    }

    private void transparentStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void startNextActivity(Class<?> activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void startNextActivityWithClear(Class<?> activity){
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void hideKeyboard(View v){
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void showKeyboardUp(View v){
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }
}