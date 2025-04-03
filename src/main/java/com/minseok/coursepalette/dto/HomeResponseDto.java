package com.minseok.coursepalette.dto;

import java.util.List;

import lombok.Data;

// 홈 화면에서
// 코스 목록 (검색 + 필터)
// 장소 목록(중복 제거)
// 를 분리해서 내려주기 위한 DTO
@Data
public class HomeResponseDto {
	// 코스 목록
	private List<CourseSimpleDto> courses;

	// 중복 제거된 장소 목록
	private List<PlaceDto> places;

	@Data
	public static class CourseSimpleDto {
		private Long courseId;
		private Long userId;
		private String title;
		private String category;
		private int favorite;
		private String createdAt;
	}

	@Data
	public static class PlaceDto {
		private String placeId;
		private String name;
		private String address;
		private String latitude;
		private String longitude;
		private String placeUrl;
	}
}
