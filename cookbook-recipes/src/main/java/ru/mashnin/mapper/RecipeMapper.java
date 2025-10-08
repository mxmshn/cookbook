package ru.mashnin.mapper;

import org.mapstruct.Mapper;
import ru.mashnin.dto.request.RecipeRequest;
import ru.mashnin.dto.response.RecipeResponse;
import ru.mashnin.entity.Recipe;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeResponse toRecipeResponse(Recipe recipe);
    Recipe toEntity(RecipeRequest recipeRequest);
}
