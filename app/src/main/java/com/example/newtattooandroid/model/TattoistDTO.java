package com.example.newtattooandroid.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
//Todo : Tattoist profile 이미지, nickname 필요
public class TattoistDTO {
    private String userId;
    private String nickName;
    private String bigAddress;
    private String smallAddress;
    private String mobile;
    private String description;
}
