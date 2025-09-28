package ru.mashnin.entity;

import jakarta.persistence.*;
import ru.mashnin.enums.Theme;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "lastname", length = 50)
    private String lastname;

    @Column(name = "firstname", length = 50)
    private String firstname;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme", nullable = false, length = 20)
    private Theme theme;

}
