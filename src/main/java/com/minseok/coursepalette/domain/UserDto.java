package com.minseok.coursepalette.domain;

public class UserDto {
	private Long userId;
	private Long kakaoId;
	private String nickname;
	private String profileImageUrl;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getKakaoId() {
		return kakaoId;
	}

	public void setKakaoId(Long kakaoId) {
		this.kakaoId = kakaoId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
}
