package ru.mashnin.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.mashnin.entity.Recipe;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, UUID>, CrudRepository<Recipe, UUID> {

    @Override
    @EntityGraph(attributePaths = "recipeStepList")
    Optional<Recipe> findById(UUID uuid);
}
