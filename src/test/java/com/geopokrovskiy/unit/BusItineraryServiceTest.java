package com.geopokrovskiy.unit;

import com.geopokrovskiy.collections.Pair;
import com.geopokrovskiy.service.BusItineraryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BusItineraryServiceTest {

    @MockitoBean
    private Map<Integer, Set<Pair<Integer, Integer>>> busStopItinerariesSetMap;

    @Autowired
    private BusItineraryServiceImpl busItineraryService;

    @BeforeEach
    void setUp() {
        Map<Integer, Set<Pair<Integer, Integer>>> busStopItinerariesSetMap = new HashMap<>();

        // Test input
        // 0 0 1 2 3 4
        // 1 3 1 6 5
        // 2 0 6 4
        Set<Pair<Integer, Integer>> stop0 = new HashSet<>();
        stop0.add(new Pair<>(0, 1));
        stop0.add(new Pair<>(2, 1));
        busStopItinerariesSetMap.put(0, stop0);

        Set<Pair<Integer, Integer>> stop1 = new HashSet<>();
        stop1.add(new Pair<>(0, 1));
        stop1.add(new Pair<>(1, 2));
        busStopItinerariesSetMap.put(1, stop1);

        Set<Pair<Integer, Integer>> stop2 = new HashSet<>();
        stop2.add(new Pair<>(0, 3));
        busStopItinerariesSetMap.put(2, stop2);

        Set<Pair<Integer, Integer>> stop3 = new HashSet<>();
        stop3.add(new Pair<>(0, 3));
        stop3.add(new Pair<>(1, 1));
        busStopItinerariesSetMap.put(3, stop3);

        Set<Pair<Integer, Integer>> stop4 = new HashSet<>();
        stop4.add(new Pair<>(0, 4));
        stop4.add(new Pair<>(2, 3));
        busStopItinerariesSetMap.put(4, stop4);

        Set<Pair<Integer, Integer>> stop5 = new HashSet<>();
        stop5.add(new Pair<>(1, 4));
        busStopItinerariesSetMap.put(5, stop5);

        Set<Pair<Integer, Integer>> stop6 = new HashSet<>();
        stop6.add(new Pair<>(1, 3));
        stop6.add(new Pair<>(2, 2));
        busStopItinerariesSetMap.put(6, stop6);

        busItineraryService = new BusItineraryServiceImpl(busStopItinerariesSetMap);
    }

    @Test
    void test_should_exist() {
        assertTrue(busItineraryService.directItineraryExists(3, 6));
    }

    @Test
    void test_should_not_exist() {
        assertFalse(busItineraryService.directItineraryExists(6, 3));
    }

    @Test
    void test_should_not_exist_for_invalid_stops() {
        assertFalse(busItineraryService.directItineraryExists(99, 100));
    }

    @Test
    void test_should_not_exist_for_same_stop() {
        assertFalse(busItineraryService.directItineraryExists(1, 1));
    }
}
