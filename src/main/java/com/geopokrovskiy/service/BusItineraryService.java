package com.geopokrovskiy.service;

public interface BusItineraryService {

    /**
     * Calculates if there is a direct itinerary between the bus stops
     *
     * @param from departure bus stop
     * @param to   destination bus stop
     * @return true if there is a direct itinerary between the bus stops
     */
    boolean directItineraryExists(Integer from, Integer to);
}
