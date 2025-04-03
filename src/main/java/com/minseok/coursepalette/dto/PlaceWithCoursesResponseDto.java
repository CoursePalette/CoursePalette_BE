package com.minseok.coursepalette.dto;

import java.util.List;

import lombok.Data;

@Data
public class PlaceWithCoursesResponseDto {
	private String placeId;
	private String name;
	private String address;
	private String latitude;
	private String longitude;
	private String placeUrl;
	private List<CourseSimpleDto> courses;

	@Data
	public static class CourseSimpleDto {
		private Long courseId;
		private UserDto user;
		private String title;
		private String category;
		private int favorite;
		private String createdAt;
	}

	@Data
	public static class UserDto {
		private Long userId;
		private String nickname;
		private String profileImageUrl;
	}
}
