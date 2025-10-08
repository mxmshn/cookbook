package ru.mashnin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecipeStepRequest {
    private String description;
    private Short stepNumber;
    private String imageUrl;
}
