package com.example.newtattooandroid.network;

import com.example.newtattooandroid.model.MainItem;
import com.example.newtattooandroid.model.TattoistDTO;
import com.example.newtattooandroid.model.TattooReviewItem;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface NetworkAPIs {
    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody);

    @Multipart
    @POST("/test")
    Call<List<MainItem>> searchImage(@Part MultipartBody.Part file);

    @GET("/allpost")
    Call<List<MainItem>> getAllPost();

    @GET("/review")
    Call<List<TattooReviewItem>> getAllReviews(@Query("postId") int id);

    @GET("/tattooist")
    Call<TattoistDTO> getTattoist(@Query("userId") String userId);
}
