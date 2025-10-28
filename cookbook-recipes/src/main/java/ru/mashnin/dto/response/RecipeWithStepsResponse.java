package ru.mashnin.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RecipeWithStepsResponse {

    private String title;

    private Short cookingTime;

    private String description;

    private Short complexity;

    private UUID authorId;

    private List<RecipeStepResponse> recipeSteps;
}
