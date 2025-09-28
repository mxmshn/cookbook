package ru.mashnin.service;

import org.springframework.data.domain.Page;
import ru.mashnin.entity.Recipe;

public interface RecipeService {
    Page<Recipe> getRecipesByPage(int page, int size);
}
