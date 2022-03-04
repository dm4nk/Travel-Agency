package com.sharashkina_kontora.travel_agency.domain;

import javax.persistence.*;

public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String city;
    String country;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "location")
    Tour tour;

}
