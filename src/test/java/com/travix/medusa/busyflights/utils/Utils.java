package com.travix.medusa.busyflights.utils;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirProviderResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetProviderResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static BusyFlightsRequest createStandardBusyFlightsRequest(){
        BusyFlightsRequest busyFlightsRequest = new BusyFlightsRequest();
        busyFlightsRequest.setOrigin("LAX");
        busyFlightsRequest.setDestination("LHR");
        busyFlightsRequest.setDepartureDate("2022-06-04");
        busyFlightsRequest.setReturnDate("2022-06-05");
        busyFlightsRequest.setNumberOfPassengers(2);

        return busyFlightsRequest;
    }

    public static List<BusyFlightsProviderResponse> createBusyFlightsProviderResponseMultipleElements(String provider){
        List<BusyFlightsProviderResponse> flights = new ArrayList<>();
        flights.add(createBusyFlightsProviderResponse("EasyJet", provider, 120.00D, "LAX", "LHR", "2022-06-04", "2022-06-05"));
        flights.add(createBusyFlightsProviderResponse("Ryanair", provider, 150.00D, "LAX", "LHR", "2022-06-04", "2022-06-05"));

        return flights;
    }

    public static List<BusyFlightsProviderResponse> createBusyFlightsProviderResponseSingleElement(String provider){
        List<BusyFlightsProviderResponse> flights = new ArrayList<>();
        flights.add(createBusyFlightsProviderResponse("EasyJet", provider, 120.00D, "LAX", "LHR", "2022-06-04", "2022-06-05"));

        return flights;
    }

    public static ResponseEntity<CrazyAirProviderResponse> createCrazyAirProviderResponseMultipleElements() {
        List<CrazyAirResponse> flights = new ArrayList<>();
        flights.add(createCrazyAirResponse("EasyJet", 120.00D, "E", "LAX", "LHR", "2022-06-04T10:15:30", "2022-06-05T10:15:30"));
        flights.add(createCrazyAirResponse("Ryanair", 140.00D, "B", "LAX", "LHR", "2022-06-04T10:15:30", "2022-06-06T10:15:30"));

        CrazyAirProviderResponse crazyAirProviderResponse = new CrazyAirProviderResponse();
        crazyAirProviderResponse.setFlights(flights);

        return ResponseEntity.ok(crazyAirProviderResponse);
    }

    public static ResponseEntity<CrazyAirProviderResponse> createCrazyAirProviderResponseSingleElement() {
        CrazyAirResponse crazyAirResponse = createCrazyAirResponse("EasyJet", 120.00D, "E", "LAX", "LHR", "2022-06-04T10:15:30", "2022-06-05T10:15:30");

        List<CrazyAirResponse> flights = new ArrayList<>();
        flights.add(crazyAirResponse);

        CrazyAirProviderResponse crazyAirProviderResponse = new CrazyAirProviderResponse();
        crazyAirProviderResponse.setFlights(flights);

        return ResponseEntity.ok(crazyAirProviderResponse);
    }

    public static ResponseEntity<CrazyAirProviderResponse> createCrazyAirProviderResponseNoElement() {
        List<CrazyAirResponse> flights = new ArrayList<>();

        CrazyAirProviderResponse crazyAirProviderResponse = new CrazyAirProviderResponse();
        crazyAirProviderResponse.setFlights(flights);

        return ResponseEntity.ok(crazyAirProviderResponse);
    }

    public static ResponseEntity<ToughJetProviderResponse> createToughJetProviderResponseMultipleElements() {
        List<ToughJetResponse> flights = new ArrayList<>();
        flights.add(createToughJetResponse("EasyJet", 120.00D, 1.20D, 1.10D, "LAX", "LHR", "2022-06-04T10:15:30Z", "2022-06-05T10:15:30Z"));
        flights.add(createToughJetResponse("Ryanair", 140.00D, 1.20D, 1.10D, "LAX", "LHR", "2022-06-04T10:15:30Z", "2022-06-06T10:15:30Z"));

        ToughJetProviderResponse toughJetProviderResponse = new ToughJetProviderResponse();
        toughJetProviderResponse.setFlights(flights);

        return ResponseEntity.ok(toughJetProviderResponse);
    }

    public static ResponseEntity<ToughJetProviderResponse> createToughJetProviderResponseSingleElement() {
        ToughJetResponse toughJetResponse = createToughJetResponse("EasyJet", 120.00D, 1.20D, 1.10D, "LAX", "LHR", "2022-06-04T10:15:30Z", "2022-06-05T10:15:30Z");

        List<ToughJetResponse> flights = new ArrayList<>();
        flights.add(toughJetResponse);

        ToughJetProviderResponse toughJetProviderResponse = new ToughJetProviderResponse();
        toughJetProviderResponse.setFlights(flights);

        return ResponseEntity.ok(toughJetProviderResponse);
    }

    public static ResponseEntity<ToughJetProviderResponse> createToughJetProviderResponseNoElement() {
        List<ToughJetResponse> flights = new ArrayList<>();

        ToughJetProviderResponse toughJetProviderResponse = new ToughJetProviderResponse();
        toughJetProviderResponse.setFlights(flights);

        return ResponseEntity.ok(toughJetProviderResponse);
    }

    private static BusyFlightsProviderResponse createBusyFlightsProviderResponse(String airline, String supplier, double fare, String departureAirportCode, String destinationAirportCode, String departureDate, String arrivalDate) {
        BusyFlightsProviderResponse busyFlightsProviderResponse = new BusyFlightsProviderResponse();
        busyFlightsProviderResponse.setAirline(airline);
        busyFlightsProviderResponse.setSupplier(supplier);
        busyFlightsProviderResponse.setFare(fare);
        busyFlightsProviderResponse.setDepartureAirportCode(departureAirportCode);
        busyFlightsProviderResponse.setDestinationAirportCode(destinationAirportCode);
        busyFlightsProviderResponse.setDepartureDate(departureDate);
        busyFlightsProviderResponse.setArrivalDate(arrivalDate);

        return busyFlightsProviderResponse;
    }

    private static CrazyAirResponse createCrazyAirResponse(String airline, double price, String cabinclass, String departureAirportCode, String destinationAirportCode, String departureDate, String arrivalDate) {
        CrazyAirResponse crazyAirResponse = new CrazyAirResponse();
        crazyAirResponse.setAirline(airline);
        crazyAirResponse.setCabinclass(cabinclass);
        crazyAirResponse.setPrice(price);
        crazyAirResponse.setDepartureAirportCode(departureAirportCode);
        crazyAirResponse.setDestinationAirportCode(destinationAirportCode);
        crazyAirResponse.setDepartureDate(departureDate);
        crazyAirResponse.setArrivalDate(arrivalDate);
        return crazyAirResponse;
    }

    private static ToughJetResponse createToughJetResponse(String carrier, double basePrice, double tax, double discount, String departureAirportName, String arrivalAirportName, String outboundDateTime,  String inboundDateTime) {
        ToughJetResponse toughJetResponse = new ToughJetResponse();
        toughJetResponse.setCarrier(carrier);
        toughJetResponse.setBasePrice(basePrice);
        toughJetResponse.setTax(tax);
        toughJetResponse.setDiscount(discount);
        toughJetResponse.setDepartureAirportName(departureAirportName);
        toughJetResponse.setArrivalAirportName(arrivalAirportName);
        toughJetResponse.setOutboundDateTime(outboundDateTime);
        toughJetResponse.setInboundDateTime(inboundDateTime);
        return toughJetResponse;
    }
}
