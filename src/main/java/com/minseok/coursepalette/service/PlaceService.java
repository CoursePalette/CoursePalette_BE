package com.minseok.coursepalette.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minseok.coursepalette.dto.course.CourseSimpleDto;
import com.minseok.coursepalette.dto.place.PlaceWithCoursesResponseDto;
import com.minseok.coursepalette.dto.user.UserDto;
import com.minseok.coursepalette.entity.CourseWithUser;
import com.minseok.coursepalette.entity.PlaceEntity;
import com.minseok.coursepalette.mapper.CourseMapper;
import com.minseok.coursepalette.mapper.PlaceMapper;

@Service
public class PlaceService {
	@Autowired
	private PlaceMapper placeMapper;

	@Autowired
	private CourseMapper courseMapper;

	public PlaceWithCoursesResponseDto getPlaceWithCourses(String placeId) {

		PlaceEntity place = placeMapper.findPlaceById(placeId);
		if (place == null) {
			return null;
		}

		// 해당 장소가 포함된 코스를 조회
		List<CourseWithUser> courseList = courseMapper.findCoursesByPlaceId(placeId);

		// 코스 데이터 DTO 변환
		List<CourseSimpleDto> courseDtos = courseList.stream().map(course -> {
			CourseSimpleDto courseDto = new CourseSimpleDto();
			courseDto.setCourseId(course.getCourseId());
			courseDto.setTitle(course.getTitle());
			courseDto.setCategory(course.getCategory());
			courseDto.setFavorite(course.getFavorite());
			courseDto.setCreatedAt(course.getCreatedAt() != null ? course.getCreatedAt().toString() : null);

			UserDto userDto = new UserDto();
			userDto.setUserId(course.getUserId());
			userDto.setNickname(course.getNickname());
			userDto.setProfileImageUrl(course.getProfileImageUrl());
			courseDto.setUser(userDto);

			return courseDto;
		}).collect(Collectors.toList());

		PlaceWithCoursesResponseDto response = new PlaceWithCoursesResponseDto();
		response.setPlaceId(place.getPlaceId());
		response.setName(place.getName());
		response.setAddress(place.getAddress());
		response.setLatitude(place.getLatitude());
		response.setLongitude(place.getLongitude());
		response.setPlaceUrl(place.getPlaceUrl());
		response.setCourses(courseDtos);
		return response;

	}
}
