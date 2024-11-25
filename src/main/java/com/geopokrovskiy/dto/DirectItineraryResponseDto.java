package com.geopokrovskiy.dto;

import lombok.Data;

@Data
public class DirectItineraryResponseDto {
    private Integer from;
    private Integer to;
    private boolean direct;
}
