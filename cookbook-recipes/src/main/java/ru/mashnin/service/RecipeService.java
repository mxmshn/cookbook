package ru.mashnin.service;

import org.springframework.data.domain.Page;
import ru.mashnin.dto.request.RecipeRequest;
import ru.mashnin.dto.response.RecipeResponse;
import ru.mashnin.dto.response.RecipeWithStepsResponse;
import ru.mashnin.entity.Recipe;

import java.util.UUID;

public interface RecipeService {
    Page<RecipeResponse> getAll(int page, int size);
    RecipeResponse create(RecipeRequest recipeRequest);
    RecipeWithStepsResponse findById(UUID id);
}
