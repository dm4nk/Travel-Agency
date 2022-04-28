package com.sharashkina_kontora.travel_agency.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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
    LocalDateTime date;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "flights")
    Set<Tour> tours = new HashSet<>();

    @Builder
    public Flight(Long id, String name, String departureAirport, String arrivalAirport, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight flight)) return false;

        return Objects.equals(id, flight.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return departureAirport +
                "\u27A2" +
                arrivalAirport;
    }
}
