package com.geopokrovskiy.rest;

import com.geopokrovskiy.dto.DirectItineraryResponseDto;
import com.geopokrovskiy.service.BusItineraryService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<DirectItineraryResponseDto> getDirectItineraryExists(@RequestParam String from, @RequestParam String to) {
        DirectItineraryResponseDto directItineraryResponseDto = new DirectItineraryResponseDto();
        try {
            Integer fromIndex = Integer.parseInt(from);
            Integer toIndex = Integer.parseInt(to);

            directItineraryResponseDto.setFrom(fromIndex);
            directItineraryResponseDto.setTo(toIndex);
            directItineraryResponseDto.setDirect(busItineraryService.directItineraryExists(fromIndex, toIndex));

            return ResponseEntity.ok(directItineraryResponseDto);
        } catch (NumberFormatException e) {
            log.error("Incorrect request with parameters {} , {}", from, to);
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }
}
