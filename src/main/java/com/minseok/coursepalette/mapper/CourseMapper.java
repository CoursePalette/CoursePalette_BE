package com.minseok.coursepalette.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.minseok.coursepalette.entity.CourseEntity;

@Mapper
public interface CourseMapper {
	@Insert("""
			INSERT INTO course (user_id, title, category)
			VALUES (#{userId}, #{title}, #{category})
		""")
	@Options(useGeneratedKeys = true, keyProperty = "courseId", keyColumn = "course_id")
	void insertCourse(CourseEntity course);

	@Insert("""
			INSERT INTO coursePlace(course_id, place_id, sequence)
			VALUES (#{courseId}, #{placeId}, #{sequence})
		""")
	void insertCoursePlace(@Param("courseId") Long courseId,
		@Param("placeId") String placeId,
		@Param("sequence") int sequence);
}
