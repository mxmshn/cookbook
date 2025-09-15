package ru.mashnin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.mashnin.enums.RoleName;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, length = 25)
    private RoleName name;
}
