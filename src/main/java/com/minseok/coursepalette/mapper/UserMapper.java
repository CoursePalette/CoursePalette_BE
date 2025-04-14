package com.minseok.coursepalette.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.minseok.coursepalette.entity.UserEntity;

@Mapper
public interface UserMapper {

	@Select("SELECT user_id, kakao_id, nickname, profile_image_url " +
		"FROM user WHERE kakao_id = #{kakaoId}")
	UserEntity findByKakaoId(Long kakaoId);

	@Insert("INSERT INTO user (kakao_id, nickname, profile_image_url)" +
		"VALUES (#{kakaoId}, #{nickname}, #{profileImageUrl})"
	)
	@Options(useGeneratedKeys = true, keyProperty = "userId")
	void insertUser(UserEntity user);

	// 사용자 프로필 업데이트
	@Update("update user set nickname = #{nickname}, profile_image_url = #{profileImageUrl} " +
		"where user_id = #{userId}")
	int updateUserProfile(@Param("userId") Long userId,
		@Param("nickname") String nickname,
		@Param("profileImageUrl") String profileImageUrl);

	// 사용자 ID로 사용자 정보 조회 메서드
	@Select("select user_id, kakao_id, nickname, profile_image_url " +
		"from user where user_id = #{userId}")
	UserEntity findById(@Param("userId") Long userId);
}
