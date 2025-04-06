package com.minseok.coursepalette.entity;

import java.util.Date;

import lombok.Data;

@Data
public class CourseEntity {
	private Long courseId;
	private Long userId;
	private String title;
	private String category;
	private Date createdAt;
}
