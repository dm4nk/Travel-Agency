package com.sharashkina_kontora.travel_agency.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String departureAirport;
    String arrivalAirport;
    LocalDate date;

}
