package com.minseok.coursepalette.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minseok.coursepalette.domain.UserDto;
import com.minseok.coursepalette.dto.KakaoUserRequest;
import com.minseok.coursepalette.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService authService;

	@PostMapping("/kakao")
	public ResponseEntity<?> handleKakaoLogin(@RequestBody KakaoUserRequest request) {

		// 유저 저장 및 업데이트
		UserDto user = authService.saveOrUpdateKakaoUser(request);

		// jwt 발급
		String token = authService.generateJwt(user);

		//응답 바디를 구성
		Map<String, Object> result = new HashMap<>();
		result.put("token", token);
		result.put("userId", user.getUserId());
		result.put("nickname", user.getNickname());
		result.put("profileImageUrl", user.getProfileImageUrl());

		return ResponseEntity.ok(result);
	}
}
