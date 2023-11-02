package com.example.RecipeManagementSystem.repo;

import com.example.RecipeManagementSystem.model.Likes;
import com.example.RecipeManagementSystem.model.Recipe;
import com.example.RecipeManagementSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILikesRepo extends JpaRepository<Likes,Integer> {
    List<Likes> findByRecipe(Recipe recipe);

    List<Likes> findByRecipeAndLiker(Recipe recipe, User liker);

}
