package com.example.newtattooandroid.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newtattooandroid.R;
import com.example.newtattooandroid.gesture.SandboxView;

public class SimulationActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.transparent);
        View view = new SandboxView(this, bitmap);

        setContentView(view);
    }
}