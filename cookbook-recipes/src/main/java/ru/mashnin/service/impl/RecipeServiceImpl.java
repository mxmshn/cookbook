package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.mashnin.entity.Recipe;
import ru.mashnin.repository.RecipeRepository;
import ru.mashnin.service.RecipeService;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    public Page<Recipe> getRecipesByPage(int page, int size) {
        return recipeRepository.findAll(Pageable.ofSize(size).withPage(page));
    }
}
