package com.minseok.coursepalette.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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

}
