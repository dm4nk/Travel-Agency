package com.sharashkina_kontora.travel_agency.domain;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "orders")

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    User user;

    @ManyToOne
    Tour tour;

    @Enumerated(EnumType.ORDINAL)
    Status status;

    @Builder
    public Order(Long id, User user, Tour tour, Status status) {
        this.id = id;
        this.user = user;
        this.tour = tour;
        this.status = status;
    }
}
