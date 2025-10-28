package ru.mashnin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mashnin.entity.Recipe;

@AllArgsConstructor
@Getter
@Setter
public class RecipeStepResponse {

    private String description;

    private Short stepNumber;

    private String imageUrl;

}
