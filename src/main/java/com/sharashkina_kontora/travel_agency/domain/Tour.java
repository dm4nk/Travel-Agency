package com.sharashkina_kontora.travel_agency.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
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

    String name;
    Integer freePlaces;
    Integer price;
    Long duration;

    @ManyToOne
    Location location;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "tour")
    Set<Order> orders = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Flight> flights = new HashSet<>();

    @Builder
    public Tour(Long id, Integer freePlaces, Integer price, Long duration, Location location, String name) {
        this.name = name;
        this.id = id;
        this.freePlaces = freePlaces;
        this.price = price;
        this.duration = duration;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tour tour)) return false;

        return Objects.equals(id, tour.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name +
                ", $" + price +
                ", " + duration + " days" +
                ", flights:" + flights;
    }
}
