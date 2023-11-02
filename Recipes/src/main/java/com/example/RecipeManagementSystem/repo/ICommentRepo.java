package com.example.RecipeManagementSystem.repo;

import com.example.RecipeManagementSystem.model.Comment;
import com.example.RecipeManagementSystem.model.Recipe;
import com.example.RecipeManagementSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepo extends JpaRepository<Comment,Integer> {
    List<Comment> findByRecipe(Recipe recipe);

    List<Comment> findByRecipeAndCommenter(Recipe recipe, User commenter);

}
