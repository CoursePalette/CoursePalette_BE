package com.minseok.coursepalette.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.minseok.coursepalette.domain.UserDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtProvider {
	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration-ms}")
	private long validityInMs;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	// userDto로 jwt 생성
	public String createToken(UserDto user) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + validityInMs);

		return Jwts.builder()
			.setSubject(String.valueOf(user.getUserId()))
			.claim("kakaoId", user.getKakaoId())
			.claim("nickname", user.getNickname())
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}
}
