package com.minseok.coursepalette.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseok.coursepalette.dto.CoursePlaceRequestDto;
import com.minseok.coursepalette.dto.CreateCourseRequestDto;
import com.minseok.coursepalette.entity.CourseEntity;
import com.minseok.coursepalette.entity.PlaceEntity;
import com.minseok.coursepalette.mapper.CourseMapper;
import com.minseok.coursepalette.mapper.PlaceMapper;

@Service
public class CourseService {
	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	private PlaceMapper placeMapper;

	@Transactional
	public Long createCourse(Long userId, CreateCourseRequestDto request) {
		// 코스 생성
		CourseEntity course = new CourseEntity();
		course.setUserId(userId);
		course.setTitle(request.getTitle());
		course.setCategory(request.getCategory());

		//DB 넣기
		courseMapper.insertCourse(course);
		Long newCourseId = course.getCourseId();

		//장소
		List<CoursePlaceRequestDto> places = request.getPlaces();
		for (CoursePlaceRequestDto p : places) {
			// placeId가 db에 있는지 확인
			PlaceEntity existingPlace = placeMapper.findPlaceById(p.getPlaceId());

			if (existingPlace == null) {
				// 없다면 place를 넣어준다.
				PlaceEntity newPlace = new PlaceEntity();
				newPlace.setPlaceId(p.getPlaceId());
				newPlace.setName(p.getName());
				newPlace.setAddress(p.getAddress());
				newPlace.setLatitude(p.getLatitude());
				newPlace.setLongitude(p.getLongitude());
				newPlace.setPlaceUrl(p.getPlaceUrl());
				placeMapper.insertPlace(newPlace);
			}

			courseMapper.insertCoursePlace(newCourseId, p.getPlaceId(), p.getSequence());
		}
		return newCourseId;
	}

}
