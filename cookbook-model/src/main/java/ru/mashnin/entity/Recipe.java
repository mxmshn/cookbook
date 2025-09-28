package ru.mashnin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String title;

    @Column(name = "cooking_time")
    private Short cookingTime;

    private String description;

    private Short complexity;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

}
