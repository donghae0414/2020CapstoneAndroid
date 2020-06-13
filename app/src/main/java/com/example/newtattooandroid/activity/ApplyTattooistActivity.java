package com.example.newtattooandroid.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.newtattooandroid.R;
import com.example.newtattooandroid.model.UserDto;

public class ApplyTattooistActivity extends AppCompatActivity {

    private UserDto userDto;

    private TextView tv_apply_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_tattooist);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getUserDto();
        tv_apply_user_name = findViewById(R.id.tv_apply_user_name);
        tv_apply_user_name.setText(userDto.getName() + " (" + userDto.getNickName() + ")");
    }

    private void getUserDto(){
        this.userDto = new UserDto();
        userDto.setUserId(getIntent().getStringExtra("userId"));
        userDto.setName(getIntent().getStringExtra("name"));
        userDto.setNickName(getIntent().getStringExtra("nickName"));
    }
}
