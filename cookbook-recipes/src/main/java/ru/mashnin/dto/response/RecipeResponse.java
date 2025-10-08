package ru.mashnin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class RecipeResponse {

    private UUID id;

    private String title;

    private Short cookingTime;

    private String description;

    private Short complexity;

    private UUID authorId;
}