package com.geopokrovskiy.config;

import com.geopokrovskiy.collections.Pair;
import com.geopokrovskiy.utils.MemoryLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@Slf4j
public class InputFileIndexer {

    @Value("classpath:/datasource/file3.txt")
    private Resource file;

    /**
     * This method indexes the input file to Map<BusStop, Set<Pair<Itinerary, Position>>>
     * where the itinerary contains the bus stop and Position is the position in the itinerary
     */
    @Bean
    public Map<Integer, Set<Pair<Integer, Integer>>> inputFileIndexing() {
        if (file == null) {
            log.error("File resource is not found!");
            return new HashMap<>();
        }
        MemoryLog.logMemoryUsage();
        log.info("Reading data from file {}", file.getFilename());
        long start = System.currentTimeMillis();
        Map<Integer, Set<Pair<Integer, Integer>>> busStopItinerariesSetMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineArray = line.split(" ");
                Integer itineraryId = Integer.parseInt(lineArray[0]);
                for (int i = 1; i < lineArray.length; i++) {
                    int busStopId = Integer.parseInt(lineArray[i]);

                    //filling Map<BusStop, Set<Pair<Itinerary, Position>>>
                    Set<Pair<Integer, Integer>> itinerariesContainingBusStop = busStopItinerariesSetMap
                            .computeIfAbsent(busStopId, (k) -> new HashSet<>());
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
        return busStopItinerariesSetMap;
    }

}
