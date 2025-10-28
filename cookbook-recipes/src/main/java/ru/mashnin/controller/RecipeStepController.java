package ru.mashnin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mashnin.dto.request.RecipeStepRequest;
import ru.mashnin.service.RecipeStepService;

import java.util.UUID;

@RestController()
@RequestMapping("/api/recipes/steps")
@RequiredArgsConstructor
public class RecipeStepController {

    private final RecipeStepService recipeStepService;

    @PostMapping("/create")
    public ResponseEntity<?> createStep(RecipeStepRequest request) {
        recipeStepService.create(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStepsByRecipeId(@PathVariable("id") String id) {
        return ResponseEntity.ok(recipeStepService.findByRecipeId(UUID.fromString(id)));
    }
}
