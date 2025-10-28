package ru.mashnin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mashnin.dto.request.RecipeRequest;
import ru.mashnin.service.RecipeService;

import java.util.UUID;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<?> getRecipes(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(recipeService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(recipeService.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRecipe(@RequestBody RecipeRequest recipeRequest) {
        return ResponseEntity.ok(recipeService.create(recipeRequest));
    }
}
