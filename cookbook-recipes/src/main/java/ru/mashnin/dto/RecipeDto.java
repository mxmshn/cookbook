package ru.mashnin.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RecipeDto {
    private String title;

    private Short cookingTime;

    private String description;

    private Short complexity;

    private UUID authorId;
}
