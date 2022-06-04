package com.travix.medusa.busyflights.service;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetProviderResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToughJetService implements BusyFlightsProvider{

    @Override
    public List<BusyFlightsProviderResponse> obtainFlights(BusyFlightsRequest busyFlightsRequest) throws Exception {

        ToughJetRequest toughJetRequest = transformRequest(busyFlightsRequest);

        ToughJetProviderResponse toughJetProviderResponse = callToughJet(toughJetRequest);

        return transformResponse(toughJetProviderResponse);
    }

    private ToughJetRequest transformRequest(BusyFlightsRequest busyFlightsRequest) {
        ToughJetRequest toughJetRequest = new ToughJetRequest();

        toughJetRequest.setFrom(busyFlightsRequest.getOrigin());
        toughJetRequest.setTo(busyFlightsRequest.getDestination());
        toughJetRequest.setOutboundDate(busyFlightsRequest.getDepartureDate());
        toughJetRequest.setInboundDate(busyFlightsRequest.getReturnDate());
        toughJetRequest.setNumberOfAdults(busyFlightsRequest.getNumberOfPassengers());

        return toughJetRequest;
    }

    private ToughJetProviderResponse callToughJet(ToughJetRequest toughJetRequest) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ToughJetProviderResponse> toughJetResponse;
        try {
            toughJetResponse = restTemplate.postForEntity("http://toughjet.co.uk/flights", toughJetRequest, ToughJetProviderResponse.class);
        } catch (RestClientException exception) {
//            logger.error("No se ha encontrado el usuario {}", id); // TODO: add logs
            throw new Exception("Error calling Crazy Air API", exception);
        }

        return toughJetResponse.getBody();
    }

    private List<BusyFlightsProviderResponse> transformResponse(ToughJetProviderResponse ToughJetProviderResponse) {

        List<BusyFlightsProviderResponse> toughJetResults = new ArrayList<>();

        for(ToughJetResponse toughJetResponse : ToughJetProviderResponse.getFlights()){
            BusyFlightsProviderResponse busyFlightsProviderResponse = new BusyFlightsProviderResponse();

            busyFlightsProviderResponse.setAirline(toughJetResponse.getCarrier());
            busyFlightsProviderResponse.setSupplier("ToughJet"); // TODO: extract to constant
            busyFlightsProviderResponse.setFare((toughJetResponse.getBasePrice() * toughJetResponse.getDiscount()) * toughJetResponse.getTax()); // TODO: Round to 2 decimals
            busyFlightsProviderResponse.setDepartureAirportCode(toughJetResponse.getDepartureAirportName());
            busyFlightsProviderResponse.setDestinationAirportCode(toughJetResponse.getArrivalAirportName());
            busyFlightsProviderResponse.setDepartureDate(toughJetResponse.getInboundDateTime()); //TODO: change date format
            busyFlightsProviderResponse.setArrivalDate(toughJetResponse.getOutboundDateTime()); //TODO: change date format

            toughJetResults.add(busyFlightsProviderResponse);
        }

        return toughJetResults;
    }
}
