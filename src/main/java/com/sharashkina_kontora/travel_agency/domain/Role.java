package com.sharashkina_kontora.travel_agency.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "role")
    List<User> users = new ArrayList<>();

    @Builder
    public Role(Long id, String name, List<User> users) {
        this.id = id;
        this.name = name;
        this.users = users == null ? new ArrayList<>() : users;
    }
}
