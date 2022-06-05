package com.travix.medusa.busyflights.services;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetProviderResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToughJetService implements BusyFlightsProvider{

    private static final Logger logger = LoggerFactory.getLogger(ToughJetService.class);

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final String SUPPLIER_NAME = "ToughJet";
    private static final String SUPPLIER_URL = "http://toughjet.co.uk/flights";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<BusyFlightsProviderResponse> obtainFlights(BusyFlightsRequest busyFlightsRequest) throws RestClientException {

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

    private ToughJetProviderResponse callToughJet(ToughJetRequest toughJetRequest) throws RestClientException {
        ResponseEntity<ToughJetProviderResponse> toughJetResponse;
        try {
            toughJetResponse = restTemplate.postForEntity(SUPPLIER_URL, toughJetRequest, ToughJetProviderResponse.class);
        } catch (RestClientException exception) {
            logger.error("Error calling Crazy Air API");
            throw new RestClientException("Error calling Crazy Air API", exception);
        }

        return toughJetResponse.getBody();
    }

    private List<BusyFlightsProviderResponse> transformResponse(ToughJetProviderResponse ToughJetProviderResponse) {

        List<BusyFlightsProviderResponse> toughJetResults = new ArrayList<>();

        for(ToughJetResponse toughJetResponse : ToughJetProviderResponse.getFlights()){
            BusyFlightsProviderResponse busyFlightsProviderResponse = new BusyFlightsProviderResponse();

            busyFlightsProviderResponse.setAirline(toughJetResponse.getCarrier());
            busyFlightsProviderResponse.setSupplier(SUPPLIER_NAME);
            busyFlightsProviderResponse.setFare(calculateFare(toughJetResponse));
            busyFlightsProviderResponse.setDepartureAirportCode(toughJetResponse.getDepartureAirportName());
            busyFlightsProviderResponse.setDestinationAirportCode(toughJetResponse.getArrivalAirportName());
            busyFlightsProviderResponse.setDepartureDate(formatDate(toughJetResponse.getOutboundDateTime()));
            busyFlightsProviderResponse.setArrivalDate(formatDate(toughJetResponse.getInboundDateTime()));

            toughJetResults.add(busyFlightsProviderResponse);
        }

        return toughJetResults;
    }

    private double calculateFare(ToughJetResponse toughJetResponse){
        return Double.parseDouble(decimalFormat.format( (toughJetResponse.getBasePrice() / toughJetResponse.getDiscount()) * toughJetResponse.getTax()) );
    }

    private String formatDate(String date){
        Instant instant = Instant.parse(date);
        return instant.atZone(ZoneId.of("Europe/London")).toLocalDate().toString();
    }
}
