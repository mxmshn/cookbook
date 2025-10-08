package ru.mashnin.mapper;

import org.mapstruct.Mapper;
import ru.mashnin.dto.request.RecipeStepRequest;
import ru.mashnin.entity.RecipeStep;

@Mapper(componentModel = "spring")
public interface RecipeStepMapper {

    RecipeStep toEntity(RecipeStepRequest request);
}
