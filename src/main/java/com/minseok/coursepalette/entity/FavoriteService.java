package com.minseok.coursepalette.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseok.coursepalette.mapper.FavoriteMapper;

@Service
public class FavoriteService {
	@Autowired
	private FavoriteMapper favoriteMapper;

	@Transactional
	public boolean favoriteCourse(Long userId, Long courseId) {
		// 이미 즐겨찾기가 되어 있는지 확인
		int count = favoriteMapper.countFavorite(userId, courseId);
		if (count > 0) {
			return false;
		} else {
			favoriteMapper.insertFavorite(userId, courseId);
			return true;
		}
	}
}
