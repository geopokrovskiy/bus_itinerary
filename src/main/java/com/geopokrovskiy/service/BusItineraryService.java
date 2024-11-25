package com.geopokrovskiy.service;


import com.geopokrovskiy.utils.Pair;
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
public class BusItineraryService {

    @Value("classpath:/datasource/file3.txt")
    private Resource file;

    private Map<Integer, Set<Pair<Integer, Integer>>> busStopItinerariesSetMap = new HashMap<>();

    /**
     * This method indexes the input file to a HashMap whose key is a bus stop id
     * and the value is a Set of Pairs <itineraryId, position> where the itinerary contains the bus stop
     */
    @PostConstruct
    private void inputFileIndexing() {
        if (file == null) {
            log.error("File resource is not found!");
            return;
        }

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
        } catch (IOException e) {
            log.error("Error reading file", e);
        }
    }

    /**
     * Calculates if there is a direct itinerary between the bus stops
     *
     * @param from departure bus stop
     * @param to   destination bus stop
     * @return true if there is a direct itinerary between the bus stops
     */
    public boolean directItineraryExists(Integer from, Integer to) {
        long start = System.currentTimeMillis();

        Set<Pair<Integer, Integer>> fromItineraries = busStopItinerariesSetMap.get(from);
        Set<Pair<Integer, Integer>> toItineraries = busStopItinerariesSetMap.get(to);

        boolean result = toItineraries.stream()
                .anyMatch(toPair -> fromItineraries.stream()
                        .anyMatch(fromPair -> fromPair.getFirst().equals(toPair.getFirst())
                                && fromPair.getSecond() < toPair.getSecond()));

        log.info("Request treatment took {} ms", System.currentTimeMillis() - start);
        return result;

    }
}