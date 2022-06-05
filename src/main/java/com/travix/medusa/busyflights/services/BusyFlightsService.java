package com.travix.medusa.busyflights.services;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BusyFlightsService {

    private static final Logger logger = LoggerFactory.getLogger(BusyFlightsService.class);

    @Autowired
    CrazyAirService crazyAir;
    @Autowired
    ToughJetService toughJet;

    public BusyFlightsResponse obtainFlights(BusyFlightsRequest busyFlightsRequest) throws ValidationException, RestClientException {

        //TODO: Other validations
        if(!checkDates(busyFlightsRequest.getDepartureDate(), busyFlightsRequest.getReturnDate())){
            logger.error("Flight dates are invalid");
            throw new ValidationException("Flight dates are invalid");
        }

        List<BusyFlightsProviderResponse> crazyAirResults = crazyAir.obtainFlights(busyFlightsRequest);
        logger.debug("Finished obtaining CrazyAir results successfully");
        List<BusyFlightsProviderResponse> toughJetResults = toughJet.obtainFlights(busyFlightsRequest);
        logger.debug("Finished obtaining ToughJet results successfully");

        List<BusyFlightsProviderResponse> busyFlightsResults = new ArrayList<>();
        busyFlightsResults.addAll(crazyAirResults);
        busyFlightsResults.addAll(toughJetResults);

        busyFlightsResults = busyFlightsResults.stream().sorted(Comparator.comparingDouble(BusyFlightsProviderResponse::getFare)).collect(Collectors.toList());
        logger.debug("Finished Obtaining flight information");

        return buildResponse(busyFlightsResults);
    }

    private BusyFlightsResponse buildResponse(List<BusyFlightsProviderResponse> flightResults){
        BusyFlightsResponse busyFlightsResponse = new BusyFlightsResponse();

        busyFlightsResponse.setFlights(flightResults);

        return busyFlightsResponse;
    }

    private boolean checkDates(String departureDate, String returnDate){
        boolean areDatesValid = false;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate departureLocalDate;
        LocalDate returnLocalDate;

        try {
            departureLocalDate = LocalDate.parse(departureDate, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return areDatesValid;
        }

        try {
            returnLocalDate = LocalDate.parse(returnDate, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return areDatesValid;
        }

        if (returnLocalDate.isAfter(departureLocalDate)){
            areDatesValid = true;
        }
        return areDatesValid;
    }
}
