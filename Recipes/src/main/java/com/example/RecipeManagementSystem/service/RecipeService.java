package com.example.RecipeManagementSystem.service;

import com.example.RecipeManagementSystem.model.Recipe;
import com.example.RecipeManagementSystem.repo.IRecipeRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecipeService {
    @Autowired
    IRecipeRepo recipeRepo;
    @Autowired
    LikesService likesService;
    @Autowired
    CommentService commentService;
    public void createRecipe(Recipe recipe) {
        recipe.setTimeStamp(LocalDateTime.now());
        recipeRepo.save(recipe);
    }


    public Recipe getRecipeById(Integer recipeId) {
        return recipeRepo.findById(recipeId).orElseThrow();
    }

    @Transactional
    public void removeById(Integer recipeId) {
        Recipe recipe = recipeRepo.findById(recipeId).orElseThrow();

        commentService.clearCommentsByRecipe(recipe);

        likesService.clearLikesByRecipe(recipe);

        recipeRepo.deleteById(recipeId);
    }

    public void updateRecipe(Integer recipeId, String time) {
        Recipe recipe = recipeRepo.findById(recipeId).orElseThrow();
        recipe.setPreparationTime(time);
        recipeRepo.save(recipe);
    }


}
