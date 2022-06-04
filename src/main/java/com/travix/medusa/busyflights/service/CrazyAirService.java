package com.travix.medusa.busyflights.service;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirProviderResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrazyAirService implements BusyFlightsProvider {

    @Override
    public List<BusyFlightsProviderResponse> obtainFlights(BusyFlightsRequest busyFlightsRequest) throws Exception {

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

    private CrazyAirProviderResponse callCrazyAir(CrazyAirRequest crazyAirRequest) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CrazyAirProviderResponse> crazyAirResponse;
        try {
            crazyAirResponse = restTemplate.postForEntity("http://crazyair.co.uk/flights", crazyAirRequest, CrazyAirProviderResponse.class);
        } catch (RestClientException exception) {
//            logger.error("No se ha encontrado el usuario {}", id); // TODO: add logs
            throw new Exception("Error calling Crazy Air API", exception);
        }

        return crazyAirResponse.getBody();
    }

    private List<BusyFlightsProviderResponse> transformResponse(CrazyAirProviderResponse crazyAirProviderResponse) {

        List<BusyFlightsProviderResponse> crazyAirResults = new ArrayList<>();

        for(CrazyAirResponse crazyAirResponse : crazyAirProviderResponse.getFlights()){
            BusyFlightsProviderResponse busyFlightsProviderResponse = new BusyFlightsProviderResponse();

            busyFlightsProviderResponse.setAirline(crazyAirResponse.getAirline());
            busyFlightsProviderResponse.setSupplier("CrazyAir"); // TODO: extract to constant
            busyFlightsProviderResponse.setFare(crazyAirResponse.getPrice()); // TODO: Round to 2 decimals
            busyFlightsProviderResponse.setDepartureAirportCode(crazyAirResponse.getDepartureAirportCode());
            busyFlightsProviderResponse.setDestinationAirportCode(crazyAirResponse.getDestinationAirportCode());
            busyFlightsProviderResponse.setDepartureDate(crazyAirResponse.getDepartureDate()); //TODO: change date format
            busyFlightsProviderResponse.setArrivalDate(crazyAirResponse.getArrivalDate()); //TODO: change date format

            crazyAirResults.add(busyFlightsProviderResponse);
        }

        return crazyAirResults;
    }
}
