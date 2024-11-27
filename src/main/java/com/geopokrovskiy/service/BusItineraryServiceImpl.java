package com.geopokrovskiy.service;


import com.geopokrovskiy.collections.Pair;
import com.geopokrovskiy.utils.MemoryLog;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Data
@Slf4j
public class BusItineraryServiceImpl implements BusItineraryService {

    @Value("classpath:/datasource/file4.txt")
    private Resource file;

    private Map<Integer, Set<Pair<Integer, Integer>>> busStopItinerariesSetMap = new HashMap<>();

    /**
     * This method indexes the input file to Map<BusStop, Set<Pair<Itinerary, Position>>>
     * where the itinerary contains the bus stop and Position is the position in the itinerary
     */
    @PostConstruct
    private void inputFileIndexing() {
        if (file == null) {
            log.error("File resource is not found!");
            return;
        }
        MemoryLog.logMemoryUsage();
        log.info("Reading data from file {}", file.getFilename());
        long start = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineArray = line.split(" ");
                Integer itineraryId = Integer.parseInt(lineArray[0]);
                for (int i = 1; i < lineArray.length; i++) {
                    int busStopId = Integer.parseInt(lineArray[i]);

                    //filling Map<BusStop, Set<Pair<Itinerary, Position>>>
                    Set<Pair<Integer, Integer>> itinerariesContainingBusStop = busStopItinerariesSetMap.getOrDefault(busStopId, new HashSet<>());
                    Pair<Integer, Integer> itineraryPositionPair = new Pair<>(itineraryId, i);
                    itinerariesContainingBusStop.add(itineraryPositionPair);
                    busStopItinerariesSetMap.put(busStopId, itinerariesContainingBusStop);
                }
            }
            long end = System.currentTimeMillis();

            log.info("{} bus stops have been indexed", busStopItinerariesSetMap.size());
            log.info("Indexing took {} ms", end - start);
            MemoryLog.logMemoryUsage();

        } catch (IOException e) {
            log.error("Error reading file", e);
        }
    }


    @Override
    public boolean directItineraryExists(Integer from, Integer to) {
        long start = System.currentTimeMillis();

        Set<Pair<Integer, Integer>> fromItineraries = busStopItinerariesSetMap.get(from);
        Set<Pair<Integer, Integer>> toItineraries = busStopItinerariesSetMap.get(to);

        MemoryLog.logMemoryUsage();
        if (fromItineraries == null || toItineraries == null) {
            log.info("Request treatment took {} ms", System.currentTimeMillis() - start);
            return false;
        }

        boolean result = toItineraries.stream()
                .anyMatch(toPair -> fromItineraries.stream()
                        .anyMatch(fromPair -> fromPair.getFirst().equals(toPair.getFirst())
                                && fromPair.getSecond() < toPair.getSecond()));

        log.info("Request treatment took {} ms", System.currentTimeMillis() - start);
        return result;
    }

}