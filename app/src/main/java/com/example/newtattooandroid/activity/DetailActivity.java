package com.example.newtattooandroid.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newtattooandroid.R;
import com.example.newtattooandroid.adapter.SliderAdapter;
import com.example.newtattooandroid.adapter.TattooReviewAdapter;
import com.example.newtattooandroid.model.SliderItem;
import com.example.newtattooandroid.model.TattoistDTO;
import com.example.newtattooandroid.model.TattooReviewItem;
import com.example.newtattooandroid.network.NetworkAPIs;
import com.example.newtattooandroid.network.NetworkClient;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {
    private SliderView sliderView;
    private RecyclerView tattooReviewsRecyclerView;
    private TattooReviewAdapter tAdapter;
    private ArrayList<TattooReviewItem> appReviews;
    //통신
    private Retrofit retrofit;
    private NetworkAPIs networkAPIs;

    //상세 리뷰
    private TextView tv_description;
    private TextView tv_clean_rating;
    private RatingBar rb_app;
    private TextView tv_tattoo_users_label;

    //타투이스트
    private TextView tv_phone;
    private TextView tv_tattooist_name;

    //Todo : 닉네임 필요함

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //retrofit
        retrofit = NetworkClient.getRetrofitClient(this);
        networkAPIs = retrofit.create(NetworkAPIs.class);

        //툴바 레이아웃
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        //도안 실행
        Button simulBtn = findViewById(R.id.btn_simulation);
        simulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SimulationActivity.class);
                getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        //뷰페이저, 슬라이드 이미지
        ArrayList<String> urls = (ArrayList<String>) getIntent().getSerializableExtra("urls");

        sliderView = findViewById(R.id.imageSlider);
        SliderAdapter sliderAdapter = new SliderAdapter(this);
        for (String url : urls) {
            sliderAdapter.addItem(new SliderItem(url));
        }
        sliderView.setSliderAdapter(sliderAdapter);

        //상세페이지 리뷰 descriptioon
        tv_tattoo_users_label = findViewById(R.id.tv_tattoo_users_label);
        tv_description = findViewById(R.id.tv_description);
        String descript = getIntent().getStringExtra("description");
        tv_description.setText(descript);

        //글의 clean 점수
        float avgCleanScore = getIntent().getFloatExtra("avgCleanScore", 0);
        float roundScore = (float)(Math.round(avgCleanScore * 10) / 10.0);

        tv_clean_rating = findViewById(R.id.tv_clean_rating);
        tv_clean_rating.setText( String.valueOf(roundScore) );

        rb_app = findViewById(R.id.rb_app);
        rb_app.setRating(roundScore);

        //타투이스트 정보 update
        String tattooistId = getIntent().getStringExtra("tattooistId");

        tv_phone = findViewById(R.id.tv_tattooist_phone);
        tv_tattooist_name = findViewById(R.id.tv_tattooist_name);

        Call<TattoistDTO> call = networkAPIs.getTattoist(tattooistId);
        call.enqueue(new Callback<TattoistDTO>() {
            @Override
            public void onResponse(Call<TattoistDTO> call, Response<TattoistDTO> response) {
                tv_phone.setText(response.body().getMobile()); // 전화번호
                tv_tattooist_name.setText(response.body().getNickName()); // 닉네임
                Log.e("TattooistSuccess", response.message());
            }

            @Override
            public void onFailure(Call<TattoistDTO> call, Throwable t) {
                Log.e("TattooistError", t.getMessage());
            }
        });

        //리뷰 데이터 리사이클러 뷰
        tattooReviewsRecyclerView = findViewById(R.id.rv_tattoo_reviews);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tattooReviewsRecyclerView.setLayoutManager(verticalLayoutManager);
        tattooReviewsRecyclerView.setHasFixedSize(true);
        appReviews = new ArrayList<>();
        tAdapter = new TattooReviewAdapter(appReviews, getApplicationContext());
        tattooReviewsRecyclerView.setAdapter(tAdapter);

        //리뷰데이터 불러오기
        loadTattooReviewsData();

    }

    private void loadTattooReviewsData() {

        int postId = getIntent().getIntExtra("postId", -1);

        if(postId != -1) {
            Call<List<TattooReviewItem>> call = networkAPIs.getAllReviews(postId);
            call.enqueue(new Callback<List<TattooReviewItem>>() {
                @Override
                public void onResponse(Call<List<TattooReviewItem>> call, Response<List<TattooReviewItem>> response) {
                    appReviews = (ArrayList<TattooReviewItem>) response.body();
                    tv_tattoo_users_label.setText(String.valueOf(appReviews.size()));
                    tAdapter.setAppReviewsList(appReviews);
                    tattooReviewsRecyclerView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<TattooReviewItem>> call, Throwable t) {
                    Log.e("DetailNetwork", t.getMessage());
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
