package com.minseok.coursepalette.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseok.coursepalette.dto.CourseDetailResponseDto;
import com.minseok.coursepalette.dto.CoursePlaceRequestDto;
import com.minseok.coursepalette.dto.CreateCourseRequestDto;
import com.minseok.coursepalette.dto.HomeResponseDto;
import com.minseok.coursepalette.entity.CourseEntity;
import com.minseok.coursepalette.entity.CourseWithUser;
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

	@Transactional
	public CourseDetailResponseDto getCourseDetail(Long courseId) {
		List<CourseDetailResponseDto.CoursePlaceDetailDto> places = courseMapper.findCoursePlacesByCourseId(courseId);
		CourseDetailResponseDto response = new CourseDetailResponseDto();
		response.setPlaces(places);
		return response;
	}

	@Transactional(readOnly = true)
	public List<HomeResponseDto.CourseSimpleDto> getMyCourses(Long userId) {
		List<CourseWithUser> courses = courseMapper.findCourseByUserId(userId);

		List<HomeResponseDto.CourseSimpleDto> courseDtos = new ArrayList<>();

		for (CourseWithUser course : courses) {
			HomeResponseDto.CourseSimpleDto dto = new HomeResponseDto.CourseSimpleDto();
			dto.setCourseId(course.getCourseId());
			dto.setTitle(course.getTitle());
			dto.setCategory(course.getCategory());
			dto.setFavorite(course.getFavorite());
			dto.setCreatedAt(course.getCreatedAt() != null ? course.getCreatedAt().toString() : null);

			HomeResponseDto.UserDto userDto = new HomeResponseDto.UserDto();
			userDto.setUserId(course.getUserId());
			userDto.setNickname(course.getNickname());
			userDto.setProfileImageUrl(course.getProfileImageUrl());
			dto.setUser(userDto);

			courseDtos.add(dto);
		}
		return courseDtos;
	}

	@Transactional
	public boolean deleteCourse(Long userId, Long courseId) {
		int rowsAffected = courseMapper.deleteCourse(courseId, userId);
		return rowsAffected > 0;
	}

	// 코스 수정
	// 1. userId로 본인 코스인지 체크
	// 2. 기존 coursePlace 레코드 삭제
	// 3. places 다시 insert
	@Transactional
	public boolean updateCourse(Long userId, Long courseId, CreateCourseRequestDto request) {

		// 해당 코스가 본인 소유인지 확인
		Long foundOwnerId = courseMapper.findOwnerByCourseId(courseId);
		if (foundOwnerId == null || !foundOwnerId.equals(userId)) {
			return false;
		}

		// course 테이블 업데이트
		courseMapper.updateCourseTitleAndCategory(courseId, request.getTitle(), request.getCategory());

		// 기존 coursePlace 삭제
		courseMapper.deleteAllCoursePlaces(courseId);

		// 새로운 places insert
		request.getPlaces().forEach(p -> {
			// place 테이블에 없는 새로운 place 라면 테이블에 추가해준다.
			PlaceEntity existingPlace = placeMapper.findPlaceById(p.getPlaceId());

			if (existingPlace == null) {
				// place 테이블에 insert
				PlaceEntity newPlace = new PlaceEntity();
				newPlace.setPlaceId(p.getPlaceId());
				newPlace.setName(p.getName());
				newPlace.setAddress(p.getAddress());
				newPlace.setLatitude(p.getLatitude());
				newPlace.setLongitude(p.getLongitude());
				newPlace.setPlaceUrl(p.getPlaceUrl());
				placeMapper.insertPlace(newPlace);
			}
			courseMapper.insertCoursePlace(courseId, p.getPlaceId(), p.getSequence());
		});
		return true;

	}

	// 수정 페이지 데이터 불러올 때 사용
	@Transactional(readOnly = true)
	public CreateCourseRequestDto getCourseEditData(Long userId, Long courseId) {
		// 코스가 유저 소유인지 확인
		Long ownerId = courseMapper.findOwnerByCourseId(courseId);
		if (ownerId == null || !ownerId.equals(userId)) {
			return null;
		}

		// 코스 정보 select
		// title, category 는 courseMapper에 select 추가
		// places 는 기존 getCourseDetail과 비슷

		CourseEntity courseEntity = courseMapper.findCourseEntity(courseId);
		if (courseEntity == null) {
			return null;
		}

		// 타입이 똑같지만 CreateCourseRequestDto에 CoursePlaceReqeustDto가 있어서..
		List<CourseDetailResponseDto.CoursePlaceDetailDto> placeList = courseMapper.findCoursePlacesByCourseId(
			courseId);
		List<CoursePlaceRequestDto> placeRequests = new ArrayList<>();
		for (CourseDetailResponseDto.CoursePlaceDetailDto pd : placeList) {
			CoursePlaceRequestDto req = new CoursePlaceRequestDto();
			req.setPlaceId(pd.getPlaceId());
			req.setName(pd.getName());
			req.setAddress(pd.getAddress());
			req.setLatitude(pd.getLatitude());
			req.setLongitude(pd.getLongitude());
			req.setPlaceUrl(pd.getPlaceUrl());
			req.setSequence(pd.getSequence());
			placeRequests.add(req);
		}

		CreateCourseRequestDto result = new CreateCourseRequestDto();
		result.setTitle(courseEntity.getTitle());
		result.setCategory(courseEntity.getCategory());
		result.setPlaces(placeRequests);
		return result;
	}
}
