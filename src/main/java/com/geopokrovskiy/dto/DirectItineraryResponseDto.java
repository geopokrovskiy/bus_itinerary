package com.geopokrovskiy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DirectItineraryResponseDto {
    private int from;
    private int to;
    private boolean direct;
}
