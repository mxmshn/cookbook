package ru.mashnin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.mashnin.entity.Recipe;

import java.util.UUID;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, UUID>, CrudRepository<Recipe, UUID> {
}
