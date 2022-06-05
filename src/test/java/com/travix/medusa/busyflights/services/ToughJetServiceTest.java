package com.travix.medusa.busyflights.services;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirProviderResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetProviderResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
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
class ToughJetServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ToughJetService toughJetService;

    @Test
    public void obtainFlights_whenRequestFlights_thenOneFlightIsReturned() {

        when(restTemplate.postForEntity(anyString(), any(ToughJetRequest.class), eq(ToughJetProviderResponse.class))).thenReturn(createToughJetProviderResponseSingleElement());

        List<BusyFlightsProviderResponse> response = toughJetService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("EasyJet", response.get(0).getAirline());
        assertEquals("ToughJet", response.get(0).getSupplier());
        assertEquals(130.91D, response.get(0).getFare());
        assertEquals("LAX", response.get(0).getDepartureAirportCode());
        assertEquals("LHR", response.get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.get(0).getDepartureDate());
        assertEquals("2022-06-05", response.get(0).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenMoreThanOneFlightIsReturned() {

        when(restTemplate.postForEntity(anyString(), any(ToughJetRequest.class), eq(ToughJetProviderResponse.class))).thenReturn(createToughJetProviderResponseMultipleElements());

        List<BusyFlightsProviderResponse> response = toughJetService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.isEmpty());
        assertEquals(2, response.size());

        assertEquals("EasyJet", response.get(0).getAirline());
        assertEquals("ToughJet", response.get(0).getSupplier());
        assertEquals(130.91D, response.get(0).getFare());
        assertEquals("LAX", response.get(0).getDepartureAirportCode());
        assertEquals("LHR", response.get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.get(0).getDepartureDate());
        assertEquals("2022-06-05", response.get(0).getArrivalDate());

        assertEquals("Ryanair", response.get(1).getAirline());
        assertEquals("ToughJet", response.get(1).getSupplier());
        assertEquals(152.73D, response.get(1).getFare());
        assertEquals("LAX", response.get(1).getDepartureAirportCode());
        assertEquals("LHR", response.get(1).getDestinationAirportCode());
        assertEquals("2022-06-04", response.get(1).getDepartureDate());
        assertEquals("2022-06-06", response.get(1).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenNoFlightIsReturned() {

        when(restTemplate.postForEntity(anyString(), any(ToughJetRequest.class), eq(ToughJetProviderResponse.class))).thenReturn(createToughJetProviderResponseNoElement());

        List<BusyFlightsProviderResponse> response = toughJetService.obtainFlights(createStandardBusyFlightsRequest());

        assertTrue(response.isEmpty());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenFareIsRoundedToTwoDecimals() {

        ResponseEntity<ToughJetProviderResponse> toughJetProviderResponse = createToughJetProviderResponseSingleElement();
        toughJetProviderResponse.getBody().getFlights().get(0).setBasePrice(145.12345D);

        when(restTemplate.postForEntity(anyString(), any(ToughJetRequest.class), eq(ToughJetProviderResponse.class))).thenReturn(toughJetProviderResponse);

        List<BusyFlightsProviderResponse> response = toughJetService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("EasyJet", response.get(0).getAirline());
        assertEquals("ToughJet", response.get(0).getSupplier());
        assertEquals(158.32D, response.get(0).getFare());
        assertEquals("LAX", response.get(0).getDepartureAirportCode());
        assertEquals("LHR", response.get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.get(0).getDepartureDate());
        assertEquals("2022-06-05", response.get(0).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenToughJetCallFails_thenErrorIsReturned() {

        when(restTemplate.postForEntity(anyString(), any(ToughJetRequest.class), eq(ToughJetProviderResponse.class))).thenThrow(new RestClientException("Error calling Crazy Air API"));

        try {
            toughJetService.obtainFlights(createStandardBusyFlightsRequest());
        }catch(RestClientException exception){
            assertEquals("Error calling Crazy Air API", exception.getCause().getMessage());
        }
    }
}