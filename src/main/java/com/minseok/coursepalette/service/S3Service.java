package com.minseok.coursepalette.service;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.minseok.coursepalette.dto.s3.PresignedUrlRequestDto;
import com.minseok.coursepalette.dto.s3.PresignedUrlResponseDto;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class S3Service {

	@Autowired
	private S3Presigner s3Presigner;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	@Value("${aws.region}")
	private String region;

	public PresignedUrlResponseDto generatePresignedUrl(Long userId, PresignedUrlRequestDto request) {
		// s3에 저장될 경로 생성 (user-profiles/{userId}/{uuid}-{fileName})
		String uniqueFileName = UUID.randomUUID().toString() + "-" + request.getFileName();
		String objectKey = "user-profiles/" + userId + "/" + uniqueFileName;

		// PutObjectRequest 생성 (업로드할 객체 정보 설정)
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(objectKey)
			.contentType(request.getContentType())
			.build();

		//Presign 요청 생성 (유효 시간 설정 포함)
		PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
			.signatureDuration(Duration.ofMinutes(5)) // URL 유효 시간은 5분
			.putObjectRequest(putObjectRequest)
			.build();

		// presigned url 생성
		URL url = s3Presigner.presignPutObject(presignRequest).url();

		// 최종적으로 DB에 저장될 이미지 URL 생성
		// 형식은 https://{bucket-name}.s3.{region}.amazonaws.com/{objectKey}
		String finalImageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, objectKey);

		PresignedUrlResponseDto responseDto = new PresignedUrlResponseDto();
		responseDto.setPresignedUrl(url.toString());
		responseDto.setImageUrl(finalImageUrl); // db 저장용 url

		return responseDto;
	}
}
