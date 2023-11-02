package com.example.RecipeManagementSystem.service;

import com.example.RecipeManagementSystem.model.Comment;
import com.example.RecipeManagementSystem.model.Likes;
import com.example.RecipeManagementSystem.model.Recipe;
import com.example.RecipeManagementSystem.model.User;
import com.example.RecipeManagementSystem.repo.ICommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    ICommentRepo commentRepo;
    public void clearCommentsByRecipe(Recipe recipe) {
        List<Comment> comments = commentRepo.findByRecipe(recipe);
        commentRepo.deleteAll(comments);
    }


    public void addComment(Comment newComment) {
        commentRepo.save(newComment);
    }

    public Comment findCommentById(Integer commentId) {
        Comment comment = commentRepo.findById(commentId).orElse(null);
        return comment;
    }

    public void removeCommentById(Integer commentId) {
        commentRepo.deleteById(commentId);
    }

    public boolean isCommented(Recipe recipe, User commenter) {
        List<Comment> comments = commentRepo.findByRecipeAndCommenter(recipe,commenter);
        return (comments != null && comments.size() != 0);
    }
}
