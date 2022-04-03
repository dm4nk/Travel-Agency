package com.sharashkina_kontora.travel_agency.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String city;
    String country;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "location")
    List<Tour> tours = new ArrayList<>();

    @Builder
    public Location(Long id, String city, String country) {
        this.id = id;
        this.city = city;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location location = (Location) o;

        return id.equals(location.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
