package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mashnin.dto.request.RecipeRequest;
import ru.mashnin.dto.request.RecipeStepRequest;
import ru.mashnin.dto.response.RecipeResponse;
import ru.mashnin.entity.Recipe;
import ru.mashnin.entity.RecipeStep;
import ru.mashnin.entity.User;
import ru.mashnin.mapper.RecipeMapper;
import ru.mashnin.mapper.RecipeStepMapper;
import ru.mashnin.repository.RecipeRepository;
import ru.mashnin.service.JwtService;
import ru.mashnin.service.RecipeService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeStepMapper recipeStepMapper;
    private final JwtService jwtService;

    public Page<RecipeResponse> getAll(int page, int size) {
        return recipeRepository.findAll(Pageable.ofSize(size).withPage(page))
                .map(recipeMapper::toRecipeResponse);
    }

    @Transactional
    public RecipeResponse create(RecipeRequest recipeRequest) {

        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        UUID authorId = jwtService.getId(jwt);
        User user = new User();
        user.setId(authorId);

        Recipe recipe = recipeMapper.toEntity(recipeRequest);
        recipe.setAuthor(user);

        Recipe newRecipe = recipeRepository.save(recipe);

        if (recipeRequest.getRecipeStep() != null) {
            List<RecipeStep> recipeStepList = newRecipe.getRecipeStepList();
            List<RecipeStepRequest> recipeSteps = recipeRequest.getRecipeStep();
            Set<Short> repetitions = new HashSet<>();

            for (RecipeStepRequest recipeStep : recipeSteps) {
                Short stepNumber = recipeStep.getStepNumber();
                if (repetitions.contains(stepNumber))
                    throw new IllegalArgumentException("Шаги не могут повторяться!");
                repetitions.add(stepNumber);

                RecipeStep newRecipeStep = recipeStepMapper.toEntity(recipeStep);
                newRecipeStep.setRecipe(newRecipe);
                recipeStepList.add(newRecipeStep);
            }
        }

        RecipeResponse response = recipeMapper.toRecipeResponse(newRecipe);
        response.setAuthorId(authorId);

        return response;
    }
}
