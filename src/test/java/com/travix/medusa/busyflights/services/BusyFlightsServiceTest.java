package com.travix.medusa.busyflights.services;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirProviderResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.travix.medusa.busyflights.utils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BusyFlightsServiceTest {

    @Mock
    CrazyAirService crazyAir;
    @Mock
    ToughJetService toughJet;

    @InjectMocks
    private BusyFlightsService busyFlightsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenOneFlightPerProviderIsReturned() throws Exception {

        when(crazyAir.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(createBusyFlightsProviderResponseSingleElement("CrazyAir"));
        when(toughJet.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(createBusyFlightsProviderResponseSingleElement("ToughJet"));

        BusyFlightsResponse response = busyFlightsService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.getFlights().isEmpty());
        assertEquals(2, response.getFlights().size());

        assertEquals("EasyJet", response.getFlights().get(0).getAirline());
        assertEquals("CrazyAir", response.getFlights().get(0).getSupplier());
        assertEquals(120.0D, response.getFlights().get(0).getFare());
        assertEquals("LAX", response.getFlights().get(0).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(0).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(0).getArrivalDate());

        assertEquals("EasyJet", response.getFlights().get(1).getAirline());
        assertEquals("ToughJet", response.getFlights().get(1).getSupplier());
        assertEquals(120.0D, response.getFlights().get(1).getFare());
        assertEquals("LAX", response.getFlights().get(1).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(1).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(1).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(1).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenMoreThanOneFlightPerProviderIsReturned() throws Exception {

        when(crazyAir.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(createBusyFlightsProviderResponseMultipleElements("CrazyAir"));
        when(toughJet.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(createBusyFlightsProviderResponseMultipleElements("ToughJet"));

        BusyFlightsResponse response = busyFlightsService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.getFlights().isEmpty());
        assertEquals(4, response.getFlights().size());

        assertEquals("EasyJet", response.getFlights().get(0).getAirline());
        assertEquals("CrazyAir", response.getFlights().get(0).getSupplier());
        assertEquals(120.0D, response.getFlights().get(0).getFare());
        assertEquals("LAX", response.getFlights().get(0).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(0).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(0).getArrivalDate());

        assertEquals("EasyJet", response.getFlights().get(1).getAirline());
        assertEquals("ToughJet", response.getFlights().get(1).getSupplier());
        assertEquals(120.0D, response.getFlights().get(1).getFare());
        assertEquals("LAX", response.getFlights().get(1).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(1).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(1).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(1).getArrivalDate());

        assertEquals("Ryanair", response.getFlights().get(2).getAirline());
        assertEquals("CrazyAir", response.getFlights().get(2).getSupplier());
        assertEquals(150.0D, response.getFlights().get(2).getFare());
        assertEquals("LAX", response.getFlights().get(2).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(2).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(2).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(2).getArrivalDate());

        assertEquals("Ryanair", response.getFlights().get(3).getAirline());
        assertEquals("ToughJet", response.getFlights().get(3).getSupplier());
        assertEquals(150.0D, response.getFlights().get(3).getFare());
        assertEquals("LAX", response.getFlights().get(3).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(3).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(3).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(3).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenNoFlightPerProviderIsReturned() throws Exception {

        when(crazyAir.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(new ArrayList<>());
        when(toughJet.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(new ArrayList<>());

        BusyFlightsResponse response = busyFlightsService.obtainFlights(createStandardBusyFlightsRequest());

        assertTrue(response.getFlights().isEmpty());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenOnlyCrazyAirProviderReturnsFlights() throws Exception {

        when(crazyAir.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(createBusyFlightsProviderResponseSingleElement("CrazyAir"));
        when(toughJet.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(new ArrayList<>());

        BusyFlightsResponse response = busyFlightsService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.getFlights().isEmpty());
        assertEquals(1, response.getFlights().size());

        assertEquals("EasyJet", response.getFlights().get(0).getAirline());
        assertEquals("CrazyAir", response.getFlights().get(0).getSupplier());
        assertEquals(120.0D, response.getFlights().get(0).getFare());
        assertEquals("LAX", response.getFlights().get(0).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(0).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(0).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenOnlyToughJetProviderReturnsFlights() throws Exception {

        when(crazyAir.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(new ArrayList<>());
        when(toughJet.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(createBusyFlightsProviderResponseSingleElement("ToughJet"));

        BusyFlightsResponse response = busyFlightsService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.getFlights().isEmpty());
        assertEquals(1, response.getFlights().size());

        assertEquals("EasyJet", response.getFlights().get(0).getAirline());
        assertEquals("ToughJet", response.getFlights().get(0).getSupplier());
        assertEquals(120.0D, response.getFlights().get(0).getFare());
        assertEquals("LAX", response.getFlights().get(0).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(0).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(0).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenRequestFlights_thenProviderResultsAreInOrderOfFare() throws Exception {

        List<BusyFlightsProviderResponse> busyFlightsCrazyAirResponse = createBusyFlightsProviderResponseMultipleElements("CrazyAir");
        busyFlightsCrazyAirResponse.get(0).setFare(79.99D);
        busyFlightsCrazyAirResponse.get(1).setFare(16.25D);
        List<BusyFlightsProviderResponse> busyFlightsToughJetResponse = createBusyFlightsProviderResponseMultipleElements("ToughJet");
        busyFlightsToughJetResponse.get(0).setFare(121D);
        busyFlightsToughJetResponse.get(1).setFare(55.0D);

        when(crazyAir.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(busyFlightsCrazyAirResponse);
        when(toughJet.obtainFlights(any(BusyFlightsRequest.class))).thenReturn(busyFlightsToughJetResponse);

        BusyFlightsResponse response = busyFlightsService.obtainFlights(createStandardBusyFlightsRequest());

        assertFalse(response.getFlights().isEmpty());
        assertEquals(4, response.getFlights().size());

        assertEquals("Ryanair", response.getFlights().get(0).getAirline());
        assertEquals("CrazyAir", response.getFlights().get(0).getSupplier());
        assertEquals(16.25D, response.getFlights().get(0).getFare());
        assertEquals("LAX", response.getFlights().get(0).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(0).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(0).getArrivalDate());

        assertEquals("Ryanair", response.getFlights().get(1).getAirline());
        assertEquals("ToughJet", response.getFlights().get(1).getSupplier());
        assertEquals(55.00D, response.getFlights().get(1).getFare());
        assertEquals("LAX", response.getFlights().get(1).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(1).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(1).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(1).getArrivalDate());

        assertEquals("EasyJet", response.getFlights().get(2).getAirline());
        assertEquals("CrazyAir", response.getFlights().get(2).getSupplier());
        assertEquals(79.99D, response.getFlights().get(2).getFare());
        assertEquals("LAX", response.getFlights().get(2).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(2).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(2).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(2).getArrivalDate());

        assertEquals("EasyJet", response.getFlights().get(3).getAirline());
        assertEquals("ToughJet", response.getFlights().get(3).getSupplier());
        assertEquals(121.00D, response.getFlights().get(3).getFare());
        assertEquals("LAX", response.getFlights().get(3).getDepartureAirportCode());
        assertEquals("LHR", response.getFlights().get(3).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getFlights().get(3).getDepartureDate());
        assertEquals("2022-06-05", response.getFlights().get(3).getArrivalDate());
    }

    @Test
    public void obtainFlights_whenRequestDepartureDateIsNotValid_thenDateErrorIsThrown(){

        BusyFlightsRequest busyFlightsRequest = createStandardBusyFlightsRequest();
        busyFlightsRequest.setDepartureDate("2022-13-04");

        try {
            busyFlightsService.obtainFlights(busyFlightsRequest);
        } catch(Exception exception) {
            assertEquals("Flight dates are invalid", exception.getMessage());
        }
    }

    @Test
    public void obtainFlights_whenRequestReturnDateIsNotValid_thenDateErrorIsThrown(){

        BusyFlightsRequest busyFlightsRequest = createStandardBusyFlightsRequest();
        busyFlightsRequest.setReturnDate("2022-06-31");

        try {
            busyFlightsService.obtainFlights(busyFlightsRequest);
        } catch(Exception exception) {
            assertEquals("Flight dates are invalid", exception.getMessage());
        }
    }

    @Test
    public void obtainFlights_whenRequestDatesAreInconsistent_thenDateErrorIsThrown(){

        BusyFlightsRequest busyFlightsRequest = createStandardBusyFlightsRequest();
        busyFlightsRequest.setDepartureDate("2022-06-06");

        try {
            busyFlightsService.obtainFlights(busyFlightsRequest);
        } catch(Exception exception) {
            assertEquals("Flight dates are invalid", exception.getMessage());
        }
    }

}