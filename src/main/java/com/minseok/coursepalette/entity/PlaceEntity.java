package com.minseok.coursepalette.entity;

import lombok.Data;

@Data
public class PlaceEntity {
	private String placeId;
	private String name;
	private String address;
	private String latitude;
	private String longitude;
	private String placeUrl;
}
