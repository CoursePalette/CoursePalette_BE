package com.minseok.coursepalette.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.minseok.coursepalette.entity.CourseEntity;
import com.minseok.coursepalette.entity.CourseWithUser;

@Mapper
public interface CourseMapper {
	// 코스 등록
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

	// 검색, 카테고리 필터된 코스 목록
	// 동적 select는 xml에 구현
	List<CourseWithUser> findCoursesByFilter(@Param("search") String search,
		@Param("category") String category);

}
