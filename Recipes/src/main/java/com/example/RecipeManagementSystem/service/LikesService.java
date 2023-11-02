package com.example.RecipeManagementSystem.service;

import com.example.RecipeManagementSystem.model.Likes;
import com.example.RecipeManagementSystem.model.Recipe;
import com.example.RecipeManagementSystem.model.User;
import com.example.RecipeManagementSystem.repo.ILikesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikesService {
    @Autowired
    ILikesRepo likesRepo;
    public void clearLikesByRecipe(Recipe recipe) {
        List<Likes> likes = likesRepo.findByRecipe(recipe);
        likesRepo.deleteAll(likes);
    }

    public boolean isAlreadyLiked(Recipe recipe, User liker) {
        List<Likes> likes = likesRepo.findByRecipeAndLiker(recipe,liker);
        return (likes != null && likes.size()!=0);
    }

    public void addLike(Likes newLike) {
        likesRepo.save(newLike);
    }

    public String removeLikeByLikerAndRecipe(User liker, Recipe recipe) {
        List<Likes> likes = likesRepo.findByRecipeAndLiker(recipe,liker);
        if(!likes.isEmpty())
        {
            likesRepo.deleteAll(likes);
            return "Un-liked";
        }
        {
            return "Note liked yet, cannot dislike!!";
        }
    }
}
