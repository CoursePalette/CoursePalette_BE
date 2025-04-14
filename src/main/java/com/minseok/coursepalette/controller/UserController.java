package com.minseok.coursepalette.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minseok.coursepalette.config.JwtProvider;
import com.minseok.coursepalette.dto.s3.PresignedUrlRequestDto;
import com.minseok.coursepalette.dto.s3.PresignedUrlResponseDto;
import com.minseok.coursepalette.dto.user.UpdateProfileRequestDto;
import com.minseok.coursepalette.service.S3Service;
import com.minseok.coursepalette.service.UserService;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private S3Service s3Service;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtProvider jwtProvider;

	@Operation(summary = "프로필 이미지 업로드용 Presigned URL 생성", description = "S3에 프로필 이미지를 직접 업로드 할 수 있는 Presigned URL을 발급받습니다.")
	@SecurityRequirement(name = "BearerAuth")
	@PostMapping("/profile/presigned-url")
	public ResponseEntity<?> getPresignedUrl(
		@RequestHeader(value = "Authorization") String authorizationHeader,
		@RequestBody @Valid PresignedUrlRequestDto request) {

		Long userId = getUserIdFromToken(authorizationHeader);
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "유효하지 않은 토큰입니다."));
		}

		try {
			PresignedUrlResponseDto response = s3Service.generatePresignedUrl(userId, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("message", "Presigned URL 생성 중 오류 발생"));
		}
	}

	@Operation(summary = "사용자 프로필 업데이트", description = "사용자의 닉네임과 프로필 이미지 URL을 업데이트합니다.")
	@SecurityRequirement(name = "BearerAuth")
	@PutMapping("/profile")
	public ResponseEntity<?> updateUserProfile(
		@RequestHeader(value = "Authorization") String authorizationHeader,
		@RequestBody @Valid UpdateProfileRequestDto request
	) {
		Long userId = getUserIdFromToken(authorizationHeader);
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "유효하지 않은 토큰입니다."));
		}

		try {
			boolean success = userService.updateUserProfile(userId, request);
			if (success) {
				Map<String, Object> response = new HashMap<>();
				response.put("message", "프로필이 성공적으로 업데이트되었습니다.");
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("message", "사용자를 찾을 수 없거나 업데이트에 실패했습니다."));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "프로필 업데이트 중 오류 발생"));
		}
	}

	// 헬퍼 메서드
	private Long getUserIdFromToken(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return null;
		}
		String token = authorizationHeader.substring("Bearer ".length());
		try {
			Claims claims = jwtProvider.parseToken(token);
			return Long.valueOf(claims.getSubject());
		} catch (Exception e) {
			return null;
		}
	}
}
