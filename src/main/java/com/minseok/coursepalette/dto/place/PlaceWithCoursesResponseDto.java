package com.minseok.coursepalette.dto.place;

import java.util.List;

import com.minseok.coursepalette.dto.course.CourseSimpleDto;

import lombok.Data;

@Data
public class PlaceWithCoursesResponseDto extends PlaceDto {

	private List<CourseSimpleDto> courses;
}
