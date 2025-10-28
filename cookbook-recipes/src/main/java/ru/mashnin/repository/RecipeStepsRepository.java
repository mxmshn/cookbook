package ru.mashnin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mashnin.entity.Recipe;
import ru.mashnin.entity.RecipeStep;

import java.util.List;
import java.util.UUID;

public interface RecipeStepsRepository extends JpaRepository<RecipeStep, UUID> {
    List<RecipeStep> findByRecipe(Recipe recipe);
}
