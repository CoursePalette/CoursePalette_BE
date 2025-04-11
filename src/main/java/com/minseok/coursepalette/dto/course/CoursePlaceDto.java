package com.minseok.coursepalette.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CoursePlaceDto {
	@Schema(description = "장소 고유 ID (예: 카카오 API 제공 ID)", example = "27301827")
	private String placeId;

	@Schema(description = "장소 이름", example = "홍대 카페")
	private String name;

	@Schema(description = "장소 주소", example = "서울 마포구 어쩌구..")
	private String address;

	@Schema(description = "위도", example = "37.12345")
	private String latitude;

	@Schema(description = "경도", example = "126.98765")
	private String longitude;

	@Schema(description = "장소 상세 URL (카카오맵 등)", example = "http://place.map.kakao.com/...")
	private String placeUrl;

	@Schema(description = "해당 코스 내 순서", example = "1")
	private int sequence;
}
