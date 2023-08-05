package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {
    Map<String,Airport> airportMap=new HashMap<>();
    Map<Integer, Flight> flightMap=new HashMap<>();

    Map<Integer,Passenger>passengerMap=new HashMap<>();

    Map<Integer,List<Integer>> flightPassangerData=new HashMap<>();

    Map<Integer,Integer> bookedPassangerCount=new HashMap<>();
    public void addAirport(Airport airport) {
        airportMap.put(airport.getAirportName(), airport);
    }

    public String getLargestAirportName() {
     int maxTerminals=Integer.MIN_VALUE;
     String largeAirportName="";
     for(Airport airport: airportMap.values()){
         if(airport.getNoOfTerminals()>maxTerminals){
             maxTerminals=airport.getNoOfTerminals();
             largeAirportName=airport.getAirportName();
         }
         else if(airport.getNoOfTerminals()==maxTerminals){
             if (airport.getAirportName() != null || airport.getAirportName().compareTo(largeAirportName)<0) {
                 largeAirportName=airport.getAirportName();
             }
         }
     }
        return largeAirportName;
    }

//    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
//      double shortestDuration=Double.MAX_VALUE;
//      for(Flight flight: flightMap.values()){
//          if(flight.getFromCity() == fromCity && flight.getToCity() == toCity){
//              if(flight.getDuration()<shortestDuration){
//                  shortestDuration= flight.getDuration();
//              }
//          }
//      }
//      return shortestDuration != Double.MAX_VALUE ? shortestDuration:-1;
//    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        int totalPeople = 0;

        Airport airport = airportMap.get(airportName);
        if (airport == null) {
            return totalPeople;
        }

        for (Flight flight : flightMap.values()) {
            if (flight.getFlightDate().equals(date) && (flight.getFromCity().equals(airport.getCity()) || flight.getToCity().equals(airport.getCity()))) {
                totalPeople += flight.getMaxCapacity();
            }
        }

        return totalPeople;
    }

    public int calculateFlightFare(Integer flightId) {
        if(! flightMap.containsKey(flightId)){
            return 0;
        }
        int noOfPeopleWhoBookedAlredy=  bookedPassangerCount.put(flightId, bookedPassangerCount.getOrDefault(flightId, 0) + 1);
        return 3000+noOfPeopleWhoBookedAlredy*50;
    }

    public void addFlight(Flight flight) {
      flightMap.put(flight.getFlightId(),flight);

    }

    public List<Flight> getAllfilghts() {
        return new ArrayList<>(flightMap.values());
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        if(! flightMap.containsKey(flightId)){
            return null;
        }
        Flight flight=flightMap.get(flightId);
        City city=flight.getFromCity();
        for(Airport airport:airportMap.values()){
            if(airport.getCity().equals(city)){
                return airport.getAirportName();
            }
        }
        return null;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
      if(!flightMap.containsKey(flightId)){
          return "FAILURE";
      }
      int noOfPassangers=0;
      if(flightPassangerData.containsKey(flightId)){
          noOfPassangers= flightPassangerData.get(flightId).size();
      }
      if(noOfPassangers==0){
          return "FAILURE";
      }
     for(Flight flight: flightMap.values()){
         if(noOfPassangers >= flight.getMaxCapacity()){
             return "FAILURE";
         }
     }

     Passenger passenger=null;
     if(passengerMap.containsKey(passengerId)){
        passenger=passengerMap.get(passengerId);
     }
     if(passenger ==null){
         return "FAILURE";
     }
     bookFlight(flightId,passengerId);
     return "SUCCESS";
    }

    private void bookFlight(Integer flightId, Integer passengerId) {
        ArrayList<Integer>pass= (ArrayList<Integer>) flightPassangerData.getOrDefault(flightId,new ArrayList<>());
        pass.add(passengerId);
        flightPassangerData.put(flightId,pass);
    }

    public String addPassenger(Passenger passenger) {
        passengerMap.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        if(!flightMap.containsKey(flightId)){
            return "FAILURE";
        }
        int noOfPassangers=0;
        if(flightPassangerData.containsKey(flightId)){
            noOfPassangers= flightPassangerData.get(flightId).size();
        }
        if(noOfPassangers==0){
            return "FAILURE";
        }
        Passenger passenger=null;
        if(passengerMap.containsKey(passengerId)){
            passenger=passengerMap.get(passengerId);
        }
        if(passenger ==null){
            return "FAILURE";
        }
        cancelFlight(flightId,passengerId);
        return "SUCCESS";
    }

    private void cancelFlight(Integer flightId, Integer passengerId) {
        ArrayList<Integer>pass= (ArrayList<Integer>) flightPassangerData.getOrDefault(flightId,new ArrayList<>());
        pass.remove(passengerId);
        flightPassangerData.put(flightId,pass);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        int ans=0;
        for(Map.Entry<Integer,List<Integer>>entry:flightPassangerData.entrySet()){
            if(entry.getValue().contains(passengerId)){
             ans++;
            }
        }
        return ans;
    }
}
