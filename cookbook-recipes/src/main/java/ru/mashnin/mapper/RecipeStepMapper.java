package ru.mashnin.mapper;

import org.mapstruct.Mapper;
import ru.mashnin.dto.request.RecipeStepRequest;
import ru.mashnin.dto.response.RecipeStepResponse;
import ru.mashnin.entity.Recipe;
import ru.mashnin.entity.RecipeStep;

@Mapper(componentModel = "spring")
public interface RecipeStepMapper {

    RecipeStep toEntity(RecipeStepRequest request);

    RecipeStepResponse toRecipeStepResponse(RecipeStep recipeStep);
}
