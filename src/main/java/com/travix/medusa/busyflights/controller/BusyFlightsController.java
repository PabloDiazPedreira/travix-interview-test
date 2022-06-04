package com.travix.medusa.busyflights.controller;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.service.BusyFlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
public class BusyFlightsController {

    @Autowired
    private BusyFlightsService busyFlightsService;

    @PostMapping("/flights")
    public ResponseEntity<BusyFlightsResponse> obtainFlights(@RequestBody @Valid BusyFlightsRequest busyFlightsRequest) throws Exception {
        return ResponseEntity.ok(busyFlightsService.obtainFlights(busyFlightsRequest));
    }
}
