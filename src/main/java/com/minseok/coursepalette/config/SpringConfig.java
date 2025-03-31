package com.minseok.coursepalette.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringConfig {
	@Bean
	public OpenAPI openAPI() {
		// Bearer 인증 설정
		SecurityScheme bearerAuth = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.name("Authorization")    // 헤더 이름
			.in(SecurityScheme.In.HEADER);

		// 모든 API에 적용할 SecurityRequirement
		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList("BearerAuth");
		return new OpenAPI()
			.addSecurityItem(securityRequirement)
			.components(
				new Components().addSecuritySchemes("BearerAuth", bearerAuth)
			)
			.info(new Info()
				.title("CoursePalette API")
				.description("코스팔레트 API 문서")
				.version("v1.0.0"));

	}
}
