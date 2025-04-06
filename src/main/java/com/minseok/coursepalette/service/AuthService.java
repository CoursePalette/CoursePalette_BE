package com.minseok.coursepalette.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minseok.coursepalette.config.JwtProvider;
import com.minseok.coursepalette.dto.auth.KakaoUserRequestDto;
import com.minseok.coursepalette.entity.UserEntity;
import com.minseok.coursepalette.mapper.UserMapper;

@Service
public class AuthService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JwtProvider jwtProvider;

	// 카카오 프로필 정보 (kakaoId, nickname, profileImageUrl)을 받아서
	// db에 유저를 저장하거나 이미 존재하면 조회 반환.

	public UserEntity saveOrUpdateKakaoUser(KakaoUserRequestDto req) {
		UserEntity existing = userMapper.findByKakaoId(req.getKakaoId());
		if (existing == null) {
			// 신규 가입 처리
			existing = new UserEntity();
			existing.setKakaoId(req.getKakaoId());
			existing.setNickname(req.getNickname());
			existing.setProfileImageUrl(req.getProfileImageUrl());
			userMapper.insertUser(existing);
		}
		return existing;
	}

	public String generateJwt(UserEntity user) {
		return jwtProvider.createToken(user);
	}

}
