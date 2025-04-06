package com.minseok.coursepalette.dto.home;

import java.util.List;

import com.minseok.coursepalette.dto.course.CourseSimpleDto;
import com.minseok.coursepalette.dto.place.PlaceDto;

import lombok.Data;

@Data
public class HomeResponseDto {
	// 코스 목록
	private List<CourseSimpleDto> courses;

	// 중복 제거된 장소 목록
	private List<PlaceDto> places;
}
