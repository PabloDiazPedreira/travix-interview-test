package com.travix.medusa.busyflights.services;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirProviderResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrazyAirService implements BusyFlightsProvider {

    private static final Logger logger = LoggerFactory.getLogger(CrazyAirService.class);

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final String SUPPLIER_NAME = "CrazyAir";
    private static final String SUPPLIER_URL = "http://crazyair.co.uk/flights";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<BusyFlightsProviderResponse> obtainFlights(BusyFlightsRequest busyFlightsRequest) throws RestClientException {

        CrazyAirRequest crazyAirRequest = transformRequest(busyFlightsRequest);

        CrazyAirProviderResponse crazyAirProviderResponse = callCrazyAir(crazyAirRequest);

        return transformResponse(crazyAirProviderResponse);
    }

    private CrazyAirRequest transformRequest(BusyFlightsRequest busyFlightsRequest) {
        CrazyAirRequest crazyAirRequest = new CrazyAirRequest();

        crazyAirRequest.setOrigin(busyFlightsRequest.getOrigin());
        crazyAirRequest.setDestination(busyFlightsRequest.getDestination());
        crazyAirRequest.setDepartureDate(busyFlightsRequest.getDepartureDate());
        crazyAirRequest.setReturnDate(busyFlightsRequest.getReturnDate());
        crazyAirRequest.setPassengerCount(busyFlightsRequest.getNumberOfPassengers());

        return crazyAirRequest;
    }

    private CrazyAirProviderResponse callCrazyAir(CrazyAirRequest crazyAirRequest) throws RestClientException {
        ResponseEntity<CrazyAirProviderResponse> crazyAirResponse;
        try {
            crazyAirResponse = restTemplate.postForEntity(SUPPLIER_URL, crazyAirRequest, CrazyAirProviderResponse.class);
//            crazyAirResponse = createCrazyAirProviderResponse();
        } catch (RestClientException exception) {
            logger.error("Error calling Crazy Air API");
            throw new RestClientException("Error calling Crazy Air API", exception);
        }

        return crazyAirResponse.getBody();
    }

    private List<BusyFlightsProviderResponse> transformResponse(CrazyAirProviderResponse crazyAirProviderResponse) {

        List<BusyFlightsProviderResponse> crazyAirResults = new ArrayList<>();

        for(CrazyAirResponse crazyAirResponse : crazyAirProviderResponse.getFlights()){
            BusyFlightsProviderResponse busyFlightsProviderResponse = new BusyFlightsProviderResponse();

            busyFlightsProviderResponse.setAirline(crazyAirResponse.getAirline());
            busyFlightsProviderResponse.setSupplier(SUPPLIER_NAME);
            busyFlightsProviderResponse.setFare(roundFare(crazyAirResponse.getPrice()));
            busyFlightsProviderResponse.setDepartureAirportCode(crazyAirResponse.getDepartureAirportCode());
            busyFlightsProviderResponse.setDestinationAirportCode(crazyAirResponse.getDestinationAirportCode());
            busyFlightsProviderResponse.setDepartureDate(formatDate(crazyAirResponse.getDepartureDate()));
            busyFlightsProviderResponse.setArrivalDate(formatDate(crazyAirResponse.getArrivalDate()));

            crazyAirResults.add(busyFlightsProviderResponse);
        }

        return crazyAirResults;
    }

    private double roundFare(double fare){
        return Double.parseDouble(decimalFormat.format(fare));
    }

    private String formatDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString();
    }
}
