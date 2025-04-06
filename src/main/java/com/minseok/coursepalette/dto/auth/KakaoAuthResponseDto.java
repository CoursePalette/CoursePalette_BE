package com.minseok.coursepalette.dto.auth;

import lombok.Data;

@Data
public class KakaoAuthResponseDto {
	private String token;
	private Long userId;
	private String nickname;
	private String profileImageUrl;
}
