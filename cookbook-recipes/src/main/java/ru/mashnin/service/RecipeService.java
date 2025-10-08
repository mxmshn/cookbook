package ru.mashnin.service;

import org.springframework.data.domain.Page;
import ru.mashnin.dto.request.RecipeRequest;
import ru.mashnin.dto.response.RecipeResponse;
import ru.mashnin.entity.Recipe;

public interface RecipeService {
    Page<RecipeResponse> getAll(int page, int size);
    RecipeResponse create(RecipeRequest recipeRequest);

}
