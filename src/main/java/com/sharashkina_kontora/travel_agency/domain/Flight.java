package com.sharashkina_kontora.travel_agency.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "flights")

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String departureAirport;
    String arrivalAirport;
    LocalDate date;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "flights")
    Set<Tour> tours = new HashSet<>();

    @Builder
    public Flight(Long id, String name, String departureAirport, String arrivalAirport, LocalDate date) {
        this.id = id;
        this.name = name;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.date = date;
    }
}
