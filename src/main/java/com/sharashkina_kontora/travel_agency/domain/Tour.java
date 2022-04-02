package com.sharashkina_kontora.travel_agency.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    List<Order> orders = new ArrayList<>();

    @Builder
    public Tour(Long id, Integer freePlaces, Integer price, Long duration, Location location) {
        this.id = id;
        this.freePlaces = freePlaces;
        this.price = price;
        this.duration = duration;
        this.location = location;
    }
}
