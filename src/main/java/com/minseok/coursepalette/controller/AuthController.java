package com.minseok.coursepalette.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minseok.coursepalette.dto.auth.KakaoAuthResponseDto;
import com.minseok.coursepalette.dto.auth.KakaoUserRequestDto;
import com.minseok.coursepalette.entity.UserEntity;
import com.minseok.coursepalette.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService authService;

	@Operation(
		summary = "카카오 로그인",
		description = "카카오 계정 정보(kakaoId, nickname 등)를 받아서 DB에 저장/조회 후 JWT를 발급"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "정상적으로 토큰 발급 및 유저 데이터 반환"),
		@ApiResponse(responseCode = "400", description = "요청 파라미터가 잘못됨"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@PostMapping("/kakao")
	public ResponseEntity<KakaoAuthResponseDto> handleKakaoLogin(@RequestBody KakaoUserRequestDto request) {

		// 유저 저장 및 업데이트
		UserEntity user = authService.saveOrUpdateKakaoUser(request);

		// jwt 발급
		String token = authService.generateJwt(user);

		//응답 바디를 구성
		KakaoAuthResponseDto result = new KakaoAuthResponseDto();
		result.setToken(token);
		result.setUserId(user.getUserId());
		result.setNickname(user.getNickname());
		result.setProfileImageUrl(user.getProfileImageUrl());

		return ResponseEntity.ok(result);
	}
}
