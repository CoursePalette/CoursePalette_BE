package com.minseok.coursepalette.dto.course;

import com.minseok.coursepalette.dto.user.UserDto;

import lombok.Data;

@Data
public class CourseSimpleDto {
	private Long courseId;
	private String title;
	private String category;
	private int favorite;
	private String createdAt;
	private UserDto user;
}
