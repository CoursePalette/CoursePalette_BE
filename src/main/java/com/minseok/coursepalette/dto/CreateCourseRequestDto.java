package com.minseok.coursepalette.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateCourseRequestDto {
	@Schema(description = "코스 제목", example = "성수동 브런치 코스")
	private String title;

	@Schema(description = "코스 카테고리", example = "데이트")
	private String category;

	@Schema(description = "장소 목록(최소 2개 이상)", example = "[{ placeId: '123', name: '카페A'...}, { ... }]")
	private List<CoursePlaceRequestDto> places;
}
