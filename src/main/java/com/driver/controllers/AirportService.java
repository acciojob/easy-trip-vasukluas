package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AirportService {
    AirportRepository airportRepository;
    public void addAirport(Airport airport) {
         airportRepository.addAirport(airport);
    }

    public String getLargestAirportName() {
        return airportRepository.getLargestAirportName();
    }

    public Double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
//        return airportRepository.getShortestDurationOfPossibleBetweenTwoCities(fromCity,toCity);
        List<Flight> flights=airportRepository.getAllfilghts();
       Double duration=Double.MAX_VALUE;
       for(Flight flight:flights){
           if (flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity)) {
               if (flight.getDuration() < duration) {
                   duration = flight.getDuration();
               }
           }
       }
       if(duration==Double.MAX_VALUE){
           return Double.valueOf("-1");
       }
       return duration;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        return airportRepository.getNumberOfPeopleOn(date,airportName);
    }

    public int calculateFlightFare(Integer flightId) {
        return airportRepository.calculateFlightFare(flightId);
    }

    public void addFlight(Flight flight) {
        airportRepository.addFlight(flight);
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        return airportRepository.getAirportNameFromFlightId(flightId);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
      return airportRepository.bookATicket(flightId,passengerId);
    }

    public String addPassenger(Passenger passenger) {
        return airportRepository.addPassenger(passenger);
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        return airportRepository.cancelATicket(flightId,passengerId);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }
}
