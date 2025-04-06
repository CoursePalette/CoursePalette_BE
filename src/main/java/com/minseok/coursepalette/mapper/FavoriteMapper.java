package com.minseok.coursepalette.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FavoriteMapper {

	//이미 즐겨찾기가 되어 있는지 확인
	@Select("SELECT COUNT(*) FROM favorite WHERE user_id = #{userId} AND course_id = #{courseId}")
	int countFavorite(@Param("userId") Long userId, @Param("courseId") Long courseId);

	// 즐겨찾기 추가
	@Insert("INSERT INTO favorite (user_id, course_id) VALUES (#{userId}, #{courseId})")
	void insertFavorite(@Param("userId") Long userId, @Param("courseId") Long courseId);

	// 즐겨찾기 삭제
	@Delete("""
		delete from favorite
		where user_id = #{userId}
			and course_id = #{courseId}
		""")
	void deleteFavorite(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
