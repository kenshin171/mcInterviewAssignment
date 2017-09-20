package com.kenshin.mcassigment.mastercardinterviewassignment.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.kenshin.mcassigment.mastercardinterviewassignment.R;
import com.kenshin.mcassigment.mastercardinterviewassignment.databinding.ActivityMainBinding;
import com.kenshin.mcassigment.mastercardinterviewassignment.viewModel.MainActivityViewModel;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

    }



}
