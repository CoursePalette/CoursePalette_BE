package com.minseok.coursepalette.dto.course;

import java.util.List;

import lombok.Data;

@Data
public class CourseDetailResponseDto {
	private List<CoursePlaceDto> places;
}
