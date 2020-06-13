package com.example.newtattooandroid.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class TattooReviewItem {

    private int cleanScore;
    private String cleanUrl = null;
    @NonNull private String date;
    @NonNull private String description;
    @NonNull private String nickName;
    private int postId;
    private int reviewId;
    private String tattooUrl1 = null;
    private String tattooUrl2 = null;
    @NonNull private String userId;
}
