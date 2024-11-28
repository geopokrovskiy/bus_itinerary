package com.geopokrovskiy.service;


import com.geopokrovskiy.collections.Pair;
import com.geopokrovskiy.utils.MemoryLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class BusItineraryServiceImpl implements BusItineraryService {

    private final Map<Integer, Set<Pair<Integer, Integer>>> busStopItinerariesSetMap;

    public BusItineraryServiceImpl(Map<Integer, Set<Pair<Integer, Integer>>> busStopItinerariesSetMap) {
        this.busStopItinerariesSetMap = busStopItinerariesSetMap;
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