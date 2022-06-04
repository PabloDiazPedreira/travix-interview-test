package com.travix.medusa.busyflights.service;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusyFlightsService {

    @Autowired
    CrazyAirService crazyAir;
    @Autowired
    ToughJetService toughJet;

    public BusyFlightsResponse obtainFlights(BusyFlightsRequest busyFlightsRequest) throws Exception {

        //Other validations
        if(!checkDates(busyFlightsRequest.getDepartureDate(), busyFlightsRequest.getReturnDate())){
            throw new Exception("flight dates are invalid");
        }

        List<BusyFlightsProviderResponse> crazyAirResults = crazyAir.obtainFlights(busyFlightsRequest);
        List<BusyFlightsProviderResponse> toughJetResults = toughJet.obtainFlights(busyFlightsRequest);

        List<BusyFlightsProviderResponse> busyFlightsResults = new ArrayList<>();
        busyFlightsResults.addAll(crazyAirResults);
        busyFlightsResults.addAll(toughJetResults);

        busyFlightsResults = busyFlightsResults.stream().sorted(Comparator.comparingDouble(BusyFlightsProviderResponse::getFare)).collect(Collectors.toList());

        return buildResponse(busyFlightsResults);
    }

//    public void comprobarUsuarioActivo(UUID id) {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<ComprobarActivoRespuesta> comprobarActivoRespuesta;
//        try {
//            comprobarActivoRespuesta = restTemplate.exchange(urlUsuarios + urlComprobarActivo + id, HttpMethod.PUT, new HttpEntity<>(null), ComprobarActivoRespuesta.class);
//        } catch (RestClientException excepcion) {
//            logger.error("No se ha encontrado el usuario {}", id);
//            throw new EmptyResultDataAccessException(1);
//        }
//
//        if (comprobarActivoRespuesta.getBody() != null && !TRUE.equals(comprobarActivoRespuesta.getBody().getEstaActivo())) {
//            logger.error("La sesion no esta activa para el usuario {}", id);
//            throw new SecurityException("El usuario no está activo");
//        }
//    }

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

    private boolean isDateValid(String date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        try {
            LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
