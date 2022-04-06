package com.sharashkina_kontora.travel_agency.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tours")

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer freePlaces;
    Integer price;
    Long duration;

    @ManyToOne
    Location location;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "tour")
    Set<Order> orders = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    Set<Flight> flights = new HashSet<>();

    @Builder
    public Tour(Long id, Integer freePlaces, Integer price, Long duration, Location location) {
        this.id = id;
        this.freePlaces = freePlaces;
        this.price = price;
        this.duration = duration;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tour)) return false;

        Tour tour = (Tour) o;

        return id.equals(tour.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
