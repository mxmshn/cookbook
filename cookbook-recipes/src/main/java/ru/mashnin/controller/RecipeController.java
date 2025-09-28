package ru.mashnin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.mashnin.dto.RecipeDto;
import ru.mashnin.service.RecipeService;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<?> getRecipes(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(recipeService.getRecipesByPage(page, size));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRecipe(@RequestBody RecipeDto recipeDto) {
        String userDetails = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userDetails);
    }
}
