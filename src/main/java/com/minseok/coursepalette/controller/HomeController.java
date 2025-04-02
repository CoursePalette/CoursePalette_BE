package com.minseok.coursepalette.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minseok.coursepalette.dto.HomeResponseDto;
import com.minseok.coursepalette.service.HomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/home")
public class HomeController {
	@Autowired
	private HomeService homeService;

	@Operation(
		summary = "홈 화면 데이터 조회",
		description = "검색어와 카테고리 필터에 따라 코스 목록과 해당 코스에 포함된 중복 없는 장소 목록을 반환한다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "정상적으로 조회됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	@GetMapping
	public HomeResponseDto getHomeData(
		@Parameter(description = "코스 제목 검색어 (LIKE 검색)", required = false)
		@RequestParam(required = false) String search,
		@Parameter(description = "코스 카테고리 (전체인 경우 필터링하지 않음)", required = false)
		@RequestParam(required = false) String category
	) {
		return homeService.getHomeCoursesAndPlaces(search, category);
	}
}
