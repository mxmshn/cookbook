package ru.mashnin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.mashnin.dto.request.RecipeRequest;
import ru.mashnin.dto.response.RecipeResponse;
import ru.mashnin.dto.response.RecipeWithStepsResponse;
import ru.mashnin.entity.Recipe;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    @Mapping(source = "author.id", target = "authorId")
    RecipeResponse toRecipeResponse(Recipe recipe);

    @Mapping(source = "author.id", target = "authorId")
    RecipeWithStepsResponse toRecipeWithStepsResponse(Recipe recipe);

    Recipe toEntity(RecipeRequest recipeRequest);

}