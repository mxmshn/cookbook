package ru.mashnin.service;

import ru.mashnin.dto.request.RecipeStepRequest;
import ru.mashnin.dto.response.RecipeStepResponse;

import java.util.List;
import java.util.UUID;

public interface RecipeStepService {
    void create(RecipeStepRequest recipeStepRequest);

    List<RecipeStepResponse> findByRecipeId(UUID id);
}
