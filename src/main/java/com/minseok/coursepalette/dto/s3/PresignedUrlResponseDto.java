package com.minseok.coursepalette.dto.s3;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PresignedUrlResponseDto {
	@Schema(description = "S3 업로드를 위한 Presigned URL")
	private String presignedUrl;

	@Schema(description = "실제 S3에 저장될 파일의 URL (업로드 완료 후 DB 저장용)")
	private String imageUrl;
}
