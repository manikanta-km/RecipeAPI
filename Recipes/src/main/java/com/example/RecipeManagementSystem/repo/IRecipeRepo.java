package com.example.RecipeManagementSystem.repo;

import com.example.RecipeManagementSystem.model.Cuisine;
import com.example.RecipeManagementSystem.model.Recipe;
import com.example.RecipeManagementSystem.model.RecipeFormat;
import com.example.RecipeManagementSystem.model.Seasoning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRecipeRepo extends JpaRepository<Recipe,Integer> {
    List<Recipe> findByRecipeNameAndRecipeFormat(String recipeName, RecipeFormat format);

    List<Recipe> findByRecipeNameAndCuisine(String recipeName, Cuisine cuisine);

    List<Recipe> findByRecipeNameAndSeasoning(String recipeName, Seasoning seasoning);

    List<Recipe> findByPreparationTimeAndServings(String prepTime, Integer servings);

    List<Recipe> findByTags(String tagName);

}
