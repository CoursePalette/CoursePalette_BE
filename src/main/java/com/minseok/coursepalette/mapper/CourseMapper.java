package com.minseok.coursepalette.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.minseok.coursepalette.dto.course.CoursePlaceDto;
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

	// 장소 id로 코스 조회
	List<CourseWithUser> findCoursesByPlaceId(@Param("placeId") String placeId);

	//코스에 포함된 장소를 순서를 포함하여 조회
	List<CoursePlaceDto> findCoursePlacesByCourseId(@Param("courseId") Long courseId);

	// userId로 코스를 조회
	@Select("""
		select c.course_id as courseId, c.title, c.category, c.created_at,  
		(select count(*) from favorite f where f.course_id = c.course_id) as favorite, 
		u.user_id as userId, u.nickname, u.profile_image_url AS profileImageUrl 
		from course c
		join user u on c.user_id = u.user_id
		where c.user_id = #{userId}
		order by c.created_at desc
		""")
	List<CourseWithUser> findCourseByUserId(@Param("userId") Long userId);

	@Delete("delete from course where course_id = #{courseId} and user_id = #{userId}")
	int deleteCourse(@Param("courseId") Long courseId, @Param("userId") Long userId);

	// 코스 주인 찾기
	Long findOwnerByCourseId(@Param("courseId") Long courseId);

	// 제목, 카ㅔㅌ고리 업데이트
	void updateCourseTitleAndCategory(
		@Param("courseId") Long courseId,
		@Param("title") String title,
		@Param("category") String categoty
	);

	// coursePlace 삭제
	void deleteAllCoursePlaces(@Param("courseId") Long courseId);

	// 코스 정보를 select (title, category 등)
	CourseEntity findCourseEntity(@Param("courseId") Long courseId);

	// 유저가 즐찾한 코스 찾기
	List<CourseWithUser> findFavoriteCoursesByUserId(@Param("userId") Long userId);
}
