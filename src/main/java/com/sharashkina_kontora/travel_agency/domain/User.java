package com.sharashkina_kontora.travel_agency.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    LocalDate birthday;
    String password;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    List<Order> order;

    @ManyToOne
    Role role;

    @Builder
    public User(Long id, String firstName, String lastName, String email, String phoneNumber, LocalDate birthday, String password, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.password = password;
        this.role = role;
    }
}