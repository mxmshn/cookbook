package ru.mashnin.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {

    private String title;

    private Short cookingTime;

    private String description;

    private Short complexity;

    private List<RecipeStepRequest> recipeStep;
}