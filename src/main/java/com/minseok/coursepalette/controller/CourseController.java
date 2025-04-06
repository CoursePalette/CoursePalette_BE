package com.minseok.coursepalette.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minseok.coursepalette.config.JwtProvider;
import com.minseok.coursepalette.dto.course.CourseDetailResponseDto;
import com.minseok.coursepalette.dto.course.CourseSimpleDto;
import com.minseok.coursepalette.dto.course.CreateCourseRequestDto;
import com.minseok.coursepalette.dto.course.CreateCourseResponseDto;
import com.minseok.coursepalette.dto.course.DeleteCourseResponseDto;
import com.minseok.coursepalette.dto.course.FavoriteRequestDto;
import com.minseok.coursepalette.dto.course.FavoriteResponseDto;
import com.minseok.coursepalette.entity.FavoriteService;
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

	@Autowired
	private FavoriteService favoriteService;

	// ========================================================================
	// ========================================================================
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

	// ========================================================================
	// ========================================================================

	@Operation(summary = "코스 상세 조회", description = "코스 아이디를 받고 해당 코스에 포함된 장소를 순서를 포함하여 반환")
	@GetMapping("/detail/{courseId}")
	public ResponseEntity<CourseDetailResponseDto> getCourseDetail(@PathVariable Long courseId) {
		CourseDetailResponseDto response = courseService.getCourseDetail(courseId);
		if (response.getPlaces() == null || response.getPlaces().isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(response);
	}

	// ========================================================================
	// ========================================================================

	@Operation(
		summary = "코스 즐겨찾기",
		description = "jwt의 userId와 body의 courseId로 즐겨찾기 진행. 이미 즐찾 되어있으면 특정 메세지 반환."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "즐겨찾기 성공 또는 이미 즐겨찾기 상태"),
		@ApiResponse(responseCode = "400", description = "토큰이 없거나 형식이 잘못됨"),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 토큰 또는 userId 추출 실패"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@SecurityRequirement(name = "BearerAuth")
	@PostMapping("/favorite")
	public ResponseEntity<FavoriteResponseDto> favoriteCourse(
		@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
		@RequestBody FavoriteRequestDto request
	) {
		FavoriteResponseDto responseDto = new FavoriteResponseDto();

		// 헤더에 토큰 있는지 확인
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			responseDto.setMessage("토큰이 없습니다.");
			return ResponseEntity.badRequest().body(responseDto);
		}
		String token = authorizationHeader.substring("Bearer ".length());

		// 토큰 파싱 후 userId를 추출한다
		Claims claims;
		try {
			claims = jwtProvider.parseToken(token);
		} catch (Exception e) {
			responseDto.setMessage("유효하지 않은 토큰입니다.");
			return ResponseEntity.status(401).body(responseDto);
		}

		Long userId;
		try {
			userId = Long.valueOf(claims.getSubject());
		} catch (NumberFormatException e) {
			responseDto.setMessage("토큰에서 userId 추출 실패.");
			return ResponseEntity.status(401).body(responseDto);
		}

		// 즐겨찾기 처리
		boolean isFavorited;
		try {
			isFavorited = favoriteService.favoriteCourse(userId, request.getCourseId());
		} catch (Exception e) {
			responseDto.setMessage("즐겨찾기 처리 중 에러 발생.");
			return ResponseEntity.status(500).body(responseDto);
		}

		if (isFavorited) {
			responseDto.setMessage("코스를 즐겨찾기 했습니다!");
		} else {
			responseDto.setMessage("이미 즐겨찾기 된 코스입니다.");
		}

		return ResponseEntity.ok(responseDto);
	}

	// ========================================================================
	// ========================================================================

	@Operation(
		summary = "내가 등록한 코스 조회",
		description = "jwt에서 userId 추출 후 해당 사용자가 등록한 코스 전체 반환"
	)
	@SecurityRequirement(name = "BearerAuth")
	@GetMapping("/mycourse")
	public ResponseEntity<List<CourseSimpleDto>> getMyCourse(
		@RequestHeader(value = "Authorization", required = true) String authorizationHeader
	) {
		// 토큰 검증
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().build();
		}

		String token = authorizationHeader.substring("Bearer ".length());
		Claims claims;
		try {
			claims = jwtProvider.parseToken(token);
		} catch (Exception e) {
			return ResponseEntity.status(401).build();
		}

		// jwt에서 userId 추출
		Long userId;
		try {
			userId = Long.valueOf(claims.getSubject());
		} catch (NumberFormatException e) {
			return ResponseEntity.status(401).build();
		}

		// 유저 코스 조회
		List<CourseSimpleDto> myCourses = courseService.getMyCourses(userId);
		return ResponseEntity.ok(myCourses);
	}

	// ========================================================================
	// ========================================================================

	@Operation(
		summary = "코스 삭제",
		description = "jwt에서 userId 추출 후 body의 courseId로 코스 삭제"
	)
	@SecurityRequirement(name = "BearerAuth")
	@DeleteMapping("/{courseId}")
	public ResponseEntity<DeleteCourseResponseDto> deleteCourse(
		@RequestHeader(value = "Authorization", required = true) String authorization,
		@PathVariable Long courseId
	) {
		DeleteCourseResponseDto response = new DeleteCourseResponseDto();

		// 토큰 검증
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			response.setMessage("토큰이 필요합니다.");
			return ResponseEntity.badRequest().body(response);
		}
		String token = authorization.substring("Bearer ".length());

		Claims claims;
		try {
			claims = jwtProvider.parseToken(token);
		} catch (Exception e) {
			response.setMessage("유효하지 않은 토큰입니다.");
			return ResponseEntity.status(401).body(response);
		}

		Long userId;
		try {
			userId = Long.valueOf(claims.getSubject());
		} catch (NumberFormatException e) {
			response.setMessage("토큰에서 userId 추출 실패했습니다.");
			return ResponseEntity.status(401).body(response);
		}

		// 코스 삭제 처리
		boolean deleted = courseService.deleteCourse(userId, courseId);
		if (deleted) {
			response.setMessage("코스를 성공적으로 삭제했습니다!");
			return ResponseEntity.ok(response);
		} else {
			response.setMessage("삭제 권한이 없거나 코스가 존재하지 않습니다.");
			return ResponseEntity.status(403).body(response);
		}
	}

	// ========================================================================
	// ========================================================================

	@Operation(summary = "코스 수정", description = "코스 ID로 수정할 데이터 받아서 업데이트")
	@SecurityRequirement(name = "BearerAuth")
	@PutMapping("/{courseId}")
	public ResponseEntity<CreateCourseResponseDto> updateCourse(
		@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
		@PathVariable Long courseId,
		@RequestBody CreateCourseRequestDto request
	) {
		CreateCourseResponseDto response = new CreateCourseResponseDto();

		// 토큰 파싱
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			response.setMessage("토큰이 필요합니다.");
			return ResponseEntity.badRequest().body(response);
		}
		String token = authorizationHeader.substring("Bearer ".length());

		Claims claims;
		try {
			claims = jwtProvider.parseToken(token);
		} catch (Exception e) {
			response.setMessage("유효하지 않은 토큰입니다.");
			return ResponseEntity.status(401).body(response);
		}

		Long userId;
		try {
			userId = Long.valueOf(claims.getSubject());
		} catch (NumberFormatException e) {
			response.setMessage("토큰에서 useId 추출 실패");
			return ResponseEntity.status(401).body(response);
		}

		boolean ok = courseService.updateCourse(userId, courseId, request);
		if (!ok) {
			response.setMessage("수정 권한이 없거나 코스가 존재하지 않습니다.");
			return ResponseEntity.status(403).body(response);
		}

		response.setCourseId(courseId);
		response.setMessage("코스 수정 완료");
		return ResponseEntity.ok(response);
	}

	// ========================================================================
	// ========================================================================

	// 수정 페이지에서 코스 정보 가져오기
	@Operation(summary = "코스 수정 페이지 초기 데이터", description = "userId가 소유자인지 확인 및 코스 데이터 반환")
	@SecurityRequirement(name = "BearerAuth")
	@GetMapping("/edit/{courseId}")
	public ResponseEntity<CreateCourseRequestDto> getCourseEditData(
		@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
		@PathVariable Long courseId
	) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(400).build();
		}
		String token = authorizationHeader.substring("Bearer ".length());
		Claims claims;

		try {
			claims = jwtProvider.parseToken(token);
		} catch (Exception e) {
			return ResponseEntity.status(401).build();
		}

		Long userId;
		try {
			userId = Long.valueOf(claims.getSubject());
		} catch (NumberFormatException e) {
			return ResponseEntity.status(401).build();
		}

		CreateCourseRequestDto dto = courseService.getCourseEditData(userId, courseId);
		if (dto == null) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(dto);

	}

	// ========================================================================
	// ========================================================================

	@Operation(
		summary = "유저가 즐겨찾기한 코스 조회",
		description = "jwt에서 userId 추출 후 해당 사용자가 즐겨찾기 한 코스 전체 반환"
	)
	@SecurityRequirement(name = "BearerAuth")
	@GetMapping("/myfavorite")
	public ResponseEntity<List<CourseSimpleDto>> getMyFavoriteCourses(
		@RequestHeader(value = "Authorization", required = true) String authorization
	) {
		// 토큰 검증
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().build();
		}

		String token = authorization.substring("Bearer ".length());
		Claims claims;
		try {
			claims = jwtProvider.parseToken(token);
		} catch (Exception e) {
			return ResponseEntity.status(401).build();
		}

		//userId 추출
		Long userId;
		try {
			userId = Long.valueOf(claims.getSubject());
		} catch (NumberFormatException e) {
			return ResponseEntity.status(401).build();
		}

		// 유저가 즐겨찾기 한 코스 조회
		List<CourseSimpleDto> favoriteCourses = courseService.getMyFavoriteCourses(userId);
		return ResponseEntity.ok(favoriteCourses);
	}

	// ========================================================================
	// ========================================================================

	@Operation(
		summary = "코스 즐겨찾기 해제",
		description = "jwt의 userId와 Pathvariable의 courseId를 사용해서 즐겨찾기 해제"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "즐겨찾기 해제 성공"),
		@ApiResponse(responseCode = "400", description = "토큰이 없거나 형식이 잘못됨"),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 토큰 또는 userId 추출 실패"),
		@ApiResponse(responseCode = "404", description = "즐겨찾기에 없는 코스일 경우"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@SecurityRequirement(name = "BearerAuth")
	@DeleteMapping("/favorite/{courseId}")
	public ResponseEntity<FavoriteResponseDto> removeFavorite(
		@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
		@PathVariable Long courseId
	) {
		FavoriteResponseDto responseDto = new FavoriteResponseDto();

		// 토큰 검사
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			responseDto.setMessage("토큰이 필요합니다.");
			return ResponseEntity.badRequest().body(responseDto);
		}
		String token = authorizationHeader.substring("Bearer ".length());

		Claims claims;
		try {
			claims = jwtProvider.parseToken(token);
		} catch (Exception e) {
			responseDto.setMessage("유효하지 않은 토큰입니다.");
			return ResponseEntity.status(401).body(responseDto);
		}

		Long userId;
		try {
			userId = Long.valueOf(claims.getSubject());
		} catch (NumberFormatException e) {
			responseDto.setMessage("토큰에서 userId 추출 실패.");
			return ResponseEntity.status(401).body(responseDto);
		}

		// 즐겨찾기 해제
		boolean ok = false;
		try {
			ok = courseService.unfavoriteCourse(userId, courseId);
		} catch (Exception e) {
			responseDto.setMessage("즐겨찾기 해제 중 오류 발생했습니다.");
			return ResponseEntity.status(500).body(responseDto);
		}

		if (!ok) {
			responseDto.setMessage("즐겨찾기에 없는 코스이거나 이미 해제되었습니다.");
			return ResponseEntity.status(404).body(responseDto);
		}

		responseDto.setMessage("코스 즐겨찾기를 해제했습니다.");
		return ResponseEntity.ok(responseDto);
	}

	// ========================================================================
	// ========================================================================

}
