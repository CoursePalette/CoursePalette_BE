package com.minseok.coursepalette.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minseok.coursepalette.dto.HomeResponseDto;
import com.minseok.coursepalette.dto.HomeResponseDto.CourseSimpleDto;
import com.minseok.coursepalette.dto.HomeResponseDto.PlaceDto;
import com.minseok.coursepalette.dto.HomeResponseDto.UserDto;
import com.minseok.coursepalette.entity.CourseWithUser;
import com.minseok.coursepalette.entity.PlaceEntity;
import com.minseok.coursepalette.mapper.CourseMapper;
import com.minseok.coursepalette.mapper.PlaceMapper;

@Service
public class HomeService {
	@Autowired
	private CourseMapper courseMapper;
	@Autowired
	private PlaceMapper placeMapper;

	// 코스 목록
	// 코스들에 속한 모든 장소 (중복 없이)

	public HomeResponseDto getHomeCoursesAndPlaces(String search, String category) {
		// 필터링된 코스 목록
		List<CourseWithUser> courseList = courseMapper.findCoursesByFilter(search, category);

		// 만약 비었다? 그렇다면 빈 값들 반환한다.
		if (courseList.isEmpty()) {
			HomeResponseDto emptyRes = new HomeResponseDto();
			emptyRes.setCourses(new ArrayList<>());
			emptyRes.setPlaces(new ArrayList<>());
			return emptyRes;
		}

		// 코스 리스트를 Dto로 변환
		List<CourseSimpleDto> courseDtos = new ArrayList<>();
		List<Long> courseIds = new ArrayList<>();

		for (CourseWithUser c : courseList) {
			courseIds.add(c.getCourseId());

			//user 정보
			UserDto userDto = new UserDto();
			userDto.setUserId(c.getUserId());
			userDto.setNickname(c.getNickname());
			userDto.setProfileImageUrl(c.getProfileImageUrl());

			CourseSimpleDto dto = new CourseSimpleDto();
			dto.setCourseId(c.getCourseId());
			dto.setUser(userDto);
			dto.setTitle(c.getTitle());
			dto.setCategory(c.getCategory());
			dto.setFavorite(c.getFavorite());
			dto.setCreatedAt(c.getCreatedAt() == null ? null : c.getCreatedAt().toString());

			courseDtos.add(dto);
		}

		// 해당 코스들에 포함된 모든 place 중복 제거
		List<PlaceEntity> placeEntities = placeMapper.findDistinctPlacesByCourseIds(courseIds);

		// placeEntities를 PlaceDto로 변환
		List<PlaceDto> placeDtos = new ArrayList<>();
		for (PlaceEntity pe : placeEntities) {
			PlaceDto pd = new PlaceDto();
			pd.setPlaceId(pe.getPlaceId());
			pd.setName(pe.getName());
			pd.setAddress(pe.getAddress());
			pd.setLatitude(pe.getLatitude());
			pd.setLongitude(pe.getLongitude());
			pd.setPlaceUrl(pe.getPlaceUrl());
			placeDtos.add(pd);
		}

		// 응답 조립
		HomeResponseDto res = new HomeResponseDto();
		res.setCourses(courseDtos);
		res.setPlaces(placeDtos);
		return res;
	}
}
