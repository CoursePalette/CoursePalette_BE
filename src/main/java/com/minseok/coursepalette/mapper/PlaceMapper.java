package com.minseok.coursepalette.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.minseok.coursepalette.entity.PlaceEntity;

@Mapper
public interface PlaceMapper {
	@Select("""
			SELECT place_id AS placeID,
			       name,
			    address,
			    latitude,
			    longitude,
			    place_url AS placeUrl
			FROM place
			WHERE place_id = #{placeId}
		""")
	PlaceEntity findPlaceById(String placeId);

	@Insert("""
			INSERT INTO place (place_id, name, address, latitude, longitude, place_url)
			VALUES (#{placeId}, #{name}, #{address}, #{latitude}, #{longitude}, #{placeUrl})
		""")
	void insertPlace(PlaceEntity place);

	// 다양한 코스에 속한 place 목록을 중복 없이 조회
	List<PlaceEntity> findDistinctPlacesByCourseIds(@Param("courseIds")
	List<Long> courseIds);
}
