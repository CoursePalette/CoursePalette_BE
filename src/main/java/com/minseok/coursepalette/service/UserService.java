package com.minseok.coursepalette.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseok.coursepalette.dto.user.UpdateProfileRequestDto;
import com.minseok.coursepalette.entity.UserEntity;
import com.minseok.coursepalette.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@Transactional
	public boolean updateUserProfile(Long userId, UpdateProfileRequestDto request) {
		int updateRows = userMapper.updateUserProfile(userId, request.getNickname(), request.getProfileImageUrl());
		return updateRows > 0; // 업데이트 성공 여부
	}

	public UserEntity getUserById(Long userId) {
		return userMapper.findById(userId);
	}
}
