package com.travix.medusa.busyflights.bdd.steps;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static com.travix.medusa.busyflights.utils.Utils.createStandardBusyFlightsRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BusyFlightsSteps {

    private BusyFlightsRequest busyFlightsRequest;
    private ResponseEntity<BusyFlightsResponse> response;
//    WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());

    @LocalServerPort
    int randomServerPort;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Given("^A user requests best flight prices$")
    public void a_user_requests_best_flight_prices() {
        busyFlightsRequest = createStandardBusyFlightsRequest();
    }

    @When("^The user sends a correct request$")
    public void the_user_sends_a_correct_request() {
        response = testRestTemplate.postForEntity("http://localhost:" + randomServerPort + "/flights", busyFlightsRequest, BusyFlightsResponse.class);
    }

    @Then("^The system will return expected result$")
    public void the_system_will_return_expected_result() {
        assertFalse(response.getBody().getFlights().isEmpty());
        assertEquals(2, response.getBody().getFlights().size());

        assertEquals("EasyJet", response.getBody().getFlights().get(0).getAirline());
        assertEquals("CrazyAir", response.getBody().getFlights().get(0).getSupplier());
        assertEquals(120.0D, response.getBody().getFlights().get(0).getFare());
        assertEquals("LAX", response.getBody().getFlights().get(0).getDepartureAirportCode());
        assertEquals("LHR", response.getBody().getFlights().get(0).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getBody().getFlights().get(0).getDepartureDate());
        assertEquals("2022-06-05", response.getBody().getFlights().get(0).getArrivalDate());

        assertEquals("EasyJet", response.getBody().getFlights().get(1).getAirline());
        assertEquals("ToughJet", response.getBody().getFlights().get(1).getSupplier());
        assertEquals(120.0D, response.getBody().getFlights().get(1).getFare());
        assertEquals("LAX", response.getBody().getFlights().get(1).getDepartureAirportCode());
        assertEquals("LHR", response.getBody().getFlights().get(1).getDestinationAirportCode());
        assertEquals("2022-06-04", response.getBody().getFlights().get(1).getDepartureDate());
        assertEquals("2022-06-05", response.getBody().getFlights().get(1).getArrivalDate());
    }
}
