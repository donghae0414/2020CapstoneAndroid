package com.example.newtattooandroid.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.newtattooandroid.R;
import com.example.newtattooandroid.model.UserDto;

public class ApplyTattooistActivity extends AppCompatActivity {

    private UserDto userDto;

    private TextView tv_apply_user_name;
    private ImageButton btn_apply_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_tattooist);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getUserDto();
        tv_apply_user_name = findViewById(R.id.tv_apply_user_name);
        tv_apply_user_name.setText(userDto.getName() + " (" + userDto.getNickName() + ")");

        btn_apply_back = findViewById(R.id.btn_apply_back);
        btn_apply_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 닫기
                finish();
            }
        });
    }

    private void getUserDto(){
        this.userDto = new UserDto();
        userDto.setUserId(getIntent().getStringExtra("userId"));
        userDto.setName(getIntent().getStringExtra("name"));
        userDto.setNickName(getIntent().getStringExtra("nickName"));
    }
}
