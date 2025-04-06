package com.minseok.coursepalette.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CourseDetailResponseDto {
	private List<CoursePlaceDetailDto> places;

	@Data
	public static class CoursePlaceDetailDto {
		@Schema(description = "장소 id", example = "123123123")
		private String placeId;

		@Schema(description = "장소 이름", example = "홍대 메가커피")
		private String name;

		@Schema(description = "장소 주소", example = "서울시 서초구 양재동 ..")
		private String address;

		@Schema(description = "위도", example = "37.12345")
		private String latitude;

		@Schema(description = "경도", example = "126.1234123")
		private String longitude;

		@Schema(description = "장소 상세 주소", example = "https://place.map.kakao.com/...")
		private String placeUrl;

		@Schema(description = "코스 내 장소의 순서", example = "1")
		private int sequence;
	}
}
