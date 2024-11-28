package com.geopokrovskiy.rest;

import com.geopokrovskiy.dto.DirectItineraryResponseDto;
import com.geopokrovskiy.service.BusItineraryService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@RequestMapping("/api")
@Slf4j
public class BusItineraryController {

    private final BusItineraryService busItineraryService;

    @GetMapping("/direct")
    public ResponseEntity<DirectItineraryResponseDto> getDirectItineraryExists(@RequestParam int from, @RequestParam int to) {
        DirectItineraryResponseDto directItineraryResponseDto =
                new DirectItineraryResponseDto(from, to, busItineraryService.directItineraryExists(from, to));
        return ResponseEntity.ok(directItineraryResponseDto);
    }
}
