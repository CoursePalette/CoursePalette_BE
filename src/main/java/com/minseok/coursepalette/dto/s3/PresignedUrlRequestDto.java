package com.minseok.coursepalette.dto.s3;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PresignedUrlRequestDto {
	@Schema(description = "업로드할 파일의 이름", example = "profile.jpg")
	private String fileName;

	@Schema(description = "업로드할 파일의 MIME 타입", example = "image/jpeg")
	private String contentType;
}
