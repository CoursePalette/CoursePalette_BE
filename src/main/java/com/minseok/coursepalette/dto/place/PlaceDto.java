package com.minseok.coursepalette.dto.place;

import lombok.Data;

@Data
public class PlaceDto {
	private String placeId;
	private String name;
	private String address;
	private String latitude;
	private String longitude;
	private String placeUrl;
}
