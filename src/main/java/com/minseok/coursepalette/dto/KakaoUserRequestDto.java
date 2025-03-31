package com.minseok.coursepalette.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class KakaoUserRequestDto {

	@Schema(description = "카카오에서 발급한 유저 고유 ID", example = "123456789")
	private Long kakaoId;

	@Schema(description = "사용자 닉네임(카카오 프로필)", example = "홍길동")
	private String nickname;

	@Schema(description = "카카오 프로필 이미지 URL", example = "http://k.kakaocdn.net/dn/...")
	private String profileImageUrl;
}
