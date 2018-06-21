package com.mr.androidvalidation;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mr.androidvalidation.databinding.ActivityMainBinding;
import com.mr.androidvalidation.dynamic_form.DynamicFormView;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModel mainViewModel = new MainViewModel();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(mainViewModel);
        binding.dynamicFormView.setViewModel(mainViewModel);
        binding.dynamicFormView.setConsumer(isValid -> mainViewModel.helloButtonEnabled.set(isValid));
        binding.submitButton.setOnClickListener(view -> {
            Toast.makeText(this, getString(R.string.congratulations), Toast.LENGTH_SHORT).show();
        });
    }
}
