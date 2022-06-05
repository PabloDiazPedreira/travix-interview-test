package com.travix.medusa.busyflights.services;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirProviderResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.travix.medusa.busyflights.utils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CrazyAirServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CrazyAirService crazyAirService;

    @Test
    public void obtainFlights_whenRequestFlights_thenOneFlightIsReturned() {

        when(restTemplate.postForEntity(anyString(), any(CrazyAirRequest.class), eq(CrazyAirProviderResponse.class))).thenReturn(createCrazyAirProviderResponseSingleElement());

        List<BusyFlightsProviderResponse> response = crazyAirService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("EasyJet", response.get(0).getAirline());
        assertEquals("CrazyAir", response.get(0).getSupplier());
        assertEquals(120.0D, response.get(0).getFare());
        assertEquals("LAX", response.get(0).getDepartureAirportCode());
        assertEquals("LHR", response.get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.get(0).getDepartureDate());
        assertEquals("2022-06-05", response.get(0).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenMoreThanOneFlightIsReturned() {

        when(restTemplate.postForEntity(anyString(), any(CrazyAirRequest.class), eq(CrazyAirProviderResponse.class))).thenReturn(createCrazyAirProviderResponseMultipleElements());

        List<BusyFlightsProviderResponse> response = crazyAirService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.isEmpty());
        assertEquals(2, response.size());

        assertEquals("EasyJet", response.get(0).getAirline());
        assertEquals("CrazyAir", response.get(0).getSupplier());
        assertEquals(120.0D, response.get(0).getFare());
        assertEquals("LAX", response.get(0).getDepartureAirportCode());
        assertEquals("LHR", response.get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.get(0).getDepartureDate());
        assertEquals("2022-06-05", response.get(0).getArrivalDate());

        assertEquals("Ryanair", response.get(1).getAirline());
        assertEquals("CrazyAir", response.get(1).getSupplier());
        assertEquals(140.0D, response.get(1).getFare());
        assertEquals("LAX", response.get(1).getDepartureAirportCode());
        assertEquals("LHR", response.get(1).getDestinationAirportCode());
        assertEquals("2022-06-04", response.get(1).getDepartureDate());
        assertEquals("2022-06-06", response.get(1).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenNoFlightIsReturned() {

        when(restTemplate.postForEntity(anyString(), any(CrazyAirRequest.class), eq(CrazyAirProviderResponse.class))).thenReturn(createCrazyAirProviderResponseNoElement());

        List<BusyFlightsProviderResponse> response = crazyAirService.obtainFlights(createStandardBusyFlightsRequest());

        assertTrue(response.isEmpty());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenFareIsRoundedToTwoDecimals() {

        ResponseEntity<CrazyAirProviderResponse> crazyAirProviderResponse = createCrazyAirProviderResponseSingleElement();
        crazyAirProviderResponse.getBody().getFlights().get(0).setPrice(145.12345D);

        when(restTemplate.postForEntity(anyString(), any(CrazyAirRequest.class), eq(CrazyAirProviderResponse.class))).thenReturn(crazyAirProviderResponse);

        List<BusyFlightsProviderResponse> response = crazyAirService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("EasyJet", response.get(0).getAirline());
        assertEquals("CrazyAir", response.get(0).getSupplier());
        assertEquals(145.12D, response.get(0).getFare());
        assertEquals("LAX", response.get(0).getDepartureAirportCode());
        assertEquals("LHR", response.get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.get(0).getDepartureDate());
        assertEquals("2022-06-05", response.get(0).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenCrazyAirCallFails_thenErrorIsReturned() {

        when(restTemplate.postForEntity(anyString(), any(CrazyAirRequest.class), eq(CrazyAirProviderResponse.class))).thenThrow(new RestClientException("Error calling Crazy Air API"));

        try {
            crazyAirService.obtainFlights(createStandardBusyFlightsRequest());
        }catch(RestClientException exception){
            assertEquals("Error calling Crazy Air API", exception.getCause().getMessage());
        }
    }
}