package com.minseok.coursepalette.entity;

import java.util.Date;

import lombok.Data;

@Data
public class CourseWithUser {
	private Long courseId;
	private String title;
	private String category;
	private Date createdAt;
	private int favorite;

	private Long userId;
	private String nickname;
	private String profileImageUrl;
}
