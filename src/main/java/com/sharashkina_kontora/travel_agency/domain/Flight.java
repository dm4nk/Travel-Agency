package com.sharashkina_kontora.travel_agency.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @ManyToMany(mappedBy = "flights")
    List<Tour> tours;

    @Builder
    public Flight(Long id, String name, String departureAirport, String arrivalAirport, LocalDate date) {
        this.id = id;
        this.name = name;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.date = date;
    }
}
