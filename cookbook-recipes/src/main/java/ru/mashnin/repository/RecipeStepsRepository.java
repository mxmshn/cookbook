package ru.mashnin.repository;

import org.springframework.data.repository.CrudRepository;
import ru.mashnin.entity.RecipeStep;

import java.util.UUID;

public interface RecipeStepsRepository extends CrudRepository<RecipeStep, UUID> {
}
