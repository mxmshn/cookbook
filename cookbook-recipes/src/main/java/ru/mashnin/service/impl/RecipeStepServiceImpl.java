package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mashnin.dto.request.RecipeStepRequest;
import ru.mashnin.dto.response.RecipeStepResponse;
import ru.mashnin.entity.Recipe;
import ru.mashnin.entity.RecipeStep;
import ru.mashnin.mapper.RecipeStepMapper;
import ru.mashnin.repository.RecipeStepsRepository;
import ru.mashnin.service.RecipeStepService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeStepServiceImpl implements RecipeStepService {

    private final RecipeStepsRepository recipeStepsRepository;
    private final RecipeStepMapper recipeStepMapper;

    public void create(RecipeStepRequest recipeStepRequest) {

        Recipe recipe = new Recipe();
        recipe.setId(recipeStepRequest.getRecipeId());
        RecipeStep recipeStep = recipeStepMapper.toEntity(recipeStepRequest);
        recipeStepsRepository.save(recipeStep);
    }

    public List<RecipeStepResponse> findByRecipeId(UUID id) {
        Recipe recipe = new Recipe();
        recipe.setId(id);
        List<RecipeStep> recipeStepList = recipeStepsRepository.findByRecipe(recipe);

        return recipeStepList.stream()
                .map(recipeStepMapper::toRecipeStepResponse)
                .toList();
    }
}
