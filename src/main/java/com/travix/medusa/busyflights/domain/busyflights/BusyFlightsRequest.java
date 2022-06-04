package com.travix.medusa.busyflights.domain.busyflights;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class BusyFlightsRequest {

    @Pattern(regexp = "^[A-Z]{3}$")
    private String origin;
    @Pattern(regexp = "^[A-Z]{3}$")
    private String destination;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private String departureDate;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private String returnDate;
    @Min(0)
    @Max(4)
    private int numberOfPassengers;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(final String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(final String returnDate) {
        this.returnDate = returnDate;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(final int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }
}
