package com.minseok.coursepalette.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FavoriteRequestDto {
	@Schema(description = "즐겨찾기 할 코스의 ID", example = "1")
	private Long courseId;
}
