package com.minseok.coursepalette.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minseok.coursepalette.dto.place.PlaceWithCoursesResponseDto;
import com.minseok.coursepalette.service.PlaceService;

@RestController
@RequestMapping("/place")
public class PlaceController {
	@Autowired
	private PlaceService placeService;

	@GetMapping("/{placeId}")
	public ResponseEntity<PlaceWithCoursesResponseDto> getPlaceWithCourses(@PathVariable String placeId) {
		PlaceWithCoursesResponseDto response = placeService.getPlaceWithCourses(placeId);
		if (response == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(response);
	}

}
