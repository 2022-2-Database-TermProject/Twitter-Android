package com.database_termproject.twitter.ui.main;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.databinding.ActivityMainBinding;
import com.database_termproject.twitter.ui.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initAfterBinding() {
        initBottomNavigation();
    }

    private void initBottomNavigation() {
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.mainBottomNavigation, navController);
        binding.mainBottomNavigation.setItemIconTintList(null);
    }
}
