package com.minseok.coursepalette.dto;

import lombok.Data;

@Data
public class KakaoUserRequest {
	private Long kakaoId;
	private String nickname;
	private String profileImageUrl;
}
