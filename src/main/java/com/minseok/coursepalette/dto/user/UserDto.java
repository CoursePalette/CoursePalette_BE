package com.minseok.coursepalette.dto.user;

import lombok.Data;

@Data
public class UserDto {
	private Long userId;
	private String nickname;
	private String profileImageUrl;
}
