package com.minseok.coursepalette.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequestDto {
	@NotBlank(message = "닉네임은 필수입니다.")
	@Size(min = 1, max = 10, message = "닉네임은 1글자 이상 10글자 이하입니다.")
	@Schema(description = "변경할 닉네임", example = "새로운 닉네임")
	private String nickname;

	@NotBlank(message = "프로필 이미지 URL은 필수입니다.")
	@Schema(description = "변경할 프로필 이미지 URL (S3 URL)", example = "https://~~")
	private String profileImageUrl;
}
