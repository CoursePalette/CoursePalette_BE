package com.minseok.coursepalette.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minseok.coursepalette.config.JwtProvider;
import com.minseok.coursepalette.dto.CreateCourseRequestDto;
import com.minseok.coursepalette.dto.CreateCourseResponseDto;
import com.minseok.coursepalette.service.CourseService;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/course")
public class CourseController {
	@Autowired
	private CourseService courseService;

	@Autowired
	private JwtProvider jwtProvider;

	@Operation(
		summary = "코스 등록",
		description = "Authorization 헤더로 Bearer 토큰을 받고, 토큰 파싱으로 userId를 추출한 뒤, 코스를 생성한다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "코스 등록 성공"),
		@ApiResponse(responseCode = "400", description = "토큰이 없거나 형식이 잘못된 경우"),
		@ApiResponse(responseCode = "401", description = "토큰이 유효하지 않거나 userId 추출 실패"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@SecurityRequirement(name = "BearerAuth")  // 스웨거에서 BearerAuth 인증 적용
	@PostMapping
	public ResponseEntity<CreateCourseResponseDto> createCourse(
		@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
		@RequestBody CreateCourseRequestDto request
	) {
		// 헤더에 토큰 있는지 우선 확인
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			CreateCourseResponseDto errorDto = new CreateCourseResponseDto();
			errorDto.setMessage("토큰이 필요합니다.");
			return ResponseEntity.badRequest().body(errorDto);
		}
		//Bearer 이후가 실제 토큰
		String token = authorizationHeader.substring("Bearer ".length());

		//토큰 파싱 후 userId 얻기
		Claims claims;
		try {
			claims = jwtProvider.parseToken(token);
		} catch (Exception e) {
			CreateCourseResponseDto errorDto = new CreateCourseResponseDto();
			errorDto.setMessage("유효하지 않은 토큰입니다.");
			return ResponseEntity.status(401).body(errorDto);
		}

		// 토큰에서 userId를 추출
		Long userId;
		try {
			userId = Long.valueOf(claims.getSubject());
		} catch (NumberFormatException e) {
			CreateCourseResponseDto errorDto = new CreateCourseResponseDto();
			errorDto.setMessage("토큰에서 userId 추출 실패");
			return ResponseEntity.status(401).body(errorDto);
		}

		// 서비스 호출
		Long courseId = courseService.createCourse(userId, request);

		//응답
		CreateCourseResponseDto responseDto = new CreateCourseResponseDto();
		responseDto.setCourseId(courseId);
		responseDto.setMessage("코스 등록 성공");
		return ResponseEntity.ok(responseDto);
	}
}
