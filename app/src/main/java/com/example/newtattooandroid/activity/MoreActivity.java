package com.example.newtattooandroid.activity;

import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newtattooandroid.R;
import com.example.newtattooandroid.adapter.TattooReviewAdapter;
import com.example.newtattooandroid.adapter.TattooReviewMoreAdapter;
import com.example.newtattooandroid.model.TattooReviewItem;
import com.example.newtattooandroid.network.NetworkAPIs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

public class MoreActivity extends AppCompatActivity {

    private RecyclerView tattooReviewMoreRecyclerView;
    private ArrayList<TattooReviewItem> appReviews;
    private TattooReviewMoreAdapter tmAdapter;

    //통신
    private Retrofit retrofit;
    private NetworkAPIs networkAPIs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        //툴바 설정, 뒤로가기 활성화, 타이틀
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("리뷰 더보기");

        //타투리뷰 더보기 리사이클러뷰 세팅
        tattooReviewMoreRecyclerView = findViewById(R.id.rv_reviews_more);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tattooReviewMoreRecyclerView.setLayoutManager(verticalLayoutManager);
        tattooReviewMoreRecyclerView.setHasFixedSize(true);
        //리뷰데이터 Intent로 전달받기
        appReviews = (ArrayList<TattooReviewItem>) getIntent().getSerializableExtra("appReviews");
        tmAdapter = new TattooReviewMoreAdapter(appReviews, getApplicationContext());
        tattooReviewMoreRecyclerView.setAdapter(tmAdapter);

    }
    private void loadTattooReviewsData() {

        int postId = getIntent().getIntExtra("postId", -1);

        if(postId != -1) {
            Call<List<TattooReviewItem>> call = networkAPIs.getAllReviews(postId);
            call.enqueue(new Callback<List<TattooReviewItem>>() {
                @Override
                public void onResponse(Call<List<TattooReviewItem>> call, Response<List<TattooReviewItem>> response) {
                    appReviews = (ArrayList<TattooReviewItem>) response.body();
                    tmAdapter.setAppReviewsList(appReviews);
                    tattooReviewMoreRecyclerView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<TattooReviewItem>> call, Throwable t) {
                    Log.e("DetailNetwork", t.getMessage());
                }
            });
        }

    }
}