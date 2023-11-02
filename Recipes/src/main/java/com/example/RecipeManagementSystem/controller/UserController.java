package com.example.RecipeManagementSystem.controller;

import com.example.RecipeManagementSystem.model.*;
import com.example.RecipeManagementSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("user/signup")
    public String userSignUp(@Valid @RequestBody User newUser)
    {
        return userService.userSignUp(newUser);
    }

    @PostMapping("user/signIn/{email}/{password}")
    public String userSignIn(@PathVariable String email, @PathVariable String password)
    {
        return userService.userSignIn(email,password);
    }

    @DeleteMapping("user/signOut")
    public String userSignOut(@RequestParam String email, @RequestParam String token)
    {
        return userService.userSignOut(email,token);
    }

    @DeleteMapping("delete/user")
    public String deleteUser(@RequestParam String email, @RequestParam String token){
        return userService.deleteUser(email,token);
    }

    @PostMapping("Recipe")
    public String createRecipe(@RequestParam String email, @RequestParam String tokenValue, @RequestBody Recipe recipe)
    {
        return userService.createRecipe(email,tokenValue,recipe);
    }

    @PutMapping("Recipe")
    public String updateRecipe(@RequestParam String email, @RequestParam String tokenValue, @RequestParam Integer recipeId, @RequestParam String time)
    {
        return userService.updateRecipe(email,tokenValue,recipeId,time);
    }

    @GetMapping("Recipe")
    public Recipe getRecipe(@RequestParam Integer recipeId)
    {
        return userService.getRecipe(recipeId);
    }

    @DeleteMapping("Recipe")
    public String deleteRecipe(@RequestParam String email, @RequestParam String tokenValue, @RequestParam Integer recipeId)
    {
        return userService.deleteRecipe(email,tokenValue,recipeId);
    }

    @PostMapping("like/recipe/{recipeId}")
    public String addLike(@RequestParam String email, @RequestParam String tokenValue, @PathVariable Integer recipeId)
    {
        return userService.addLike(email,tokenValue,recipeId);
    }

    @DeleteMapping("unlike/recipe/{recipeId}")
    public String removeLike(@RequestParam String email, @RequestParam String tokenValue, @PathVariable Integer recipeId)
    {
        return userService.removeLike(email,tokenValue,recipeId);
    }

    @PostMapping("comment/recipe/{recipeId}")
    public String addComment(@RequestParam String email, @RequestParam String tokenValue, @PathVariable Integer recipeId,@RequestBody String commentBody )
    {
        return userService.addComment(email,tokenValue,commentBody,recipeId);
    }

    @DeleteMapping("recipe/comment/{commentId}")
    public String removeComment(@RequestParam String email, @RequestParam String tokenValue,@PathVariable Integer commentId)
    {
        return userService.removeComment(email,tokenValue,commentId);
    }

    @PostMapping("follow/user/{targetUserId}")
    public String followTarget(@RequestParam String email, @RequestParam String tokenValue, @PathVariable Integer targetUserId)
    {
        return userService.followTarget(email,tokenValue,targetUserId);
    }

    @DeleteMapping("unFollow/user/{targetUserId}")
    public String unfollow(@RequestParam String email, @RequestParam String tokenValue, @PathVariable Integer targetUserId, @RequestParam Integer followId)
    {
        return userService.unFollowTarget(email,tokenValue,targetUserId,followId);
    }

    @GetMapping("recipesByDate")
    public List<Recipe> getRecipesByDate(@RequestParam String email, @RequestParam String tokenValue, @RequestParam LocalDate date)
    {
        return userService.getRecipesByDate(email,tokenValue,date);
    }

    @GetMapping("recipesByChefsName")
    public List<Recipe> getRecipesByChefsName (@RequestParam String email, @RequestParam String tokenValue, @RequestParam String chefsName){
        return  userService.getRecipesByChefsName(email,tokenValue,chefsName);
    }

    @GetMapping("recipesByName")
    public List<Recipe> getRecipesByName(@RequestParam String email, @RequestParam String tokenValue, @RequestParam String recipeName){
        return userService.getRecipesByName(email,tokenValue,recipeName);
    }

    @GetMapping("recipesByCategory")
    public List<Recipe> getRecipesByCategory(@RequestParam String email, @RequestParam String tokenValue, @RequestParam Categories category){
        return userService.getRecipesByCategory(email,tokenValue,category);
    }

    @GetMapping("recipesByCuisine")
    public List<Recipe> getRecipesByCuisine(@RequestParam String email, @RequestParam String tokenValue, @RequestParam Cuisine cuisine){
        return userService.getRecipesByCuisine(email,tokenValue,cuisine);
    }

    @GetMapping("recipesByFormat")
    public List<Recipe> getRecipesByFormat(@RequestParam String email, @RequestParam String tokenValue, @RequestParam RecipeFormat recipeFormat){
        return userService.getRecipesByFormat(email,tokenValue,recipeFormat);
    }

    @GetMapping("recipesByPreparationTime")
    public List<Recipe> getRecipesByTime(@RequestParam String email, @RequestParam String tokenValue, @RequestParam String time){
        return userService.getRecipesByTime(email,tokenValue,time);
    }

    @GetMapping("recipesByChefsNameAndRecipeName")
    public List<Recipe> getRecipesByChefsNameAndRecipeName(@RequestParam String email, @RequestParam String tokenValue, @RequestParam String chefsName, @RequestParam String recipeName){
        return userService.getRecipesByChefsNameAndRecipeName(email,tokenValue,chefsName,recipeName);
    }

    @GetMapping("recipesByRecipeNameAndFormat")
    public List<Recipe> getRecipesByRecipeNameAndFormat(@RequestParam String recipeName, @RequestParam RecipeFormat format){
        return userService.getRecipesByRecipeNameAndFormat(recipeName,format);
    }

    @GetMapping("recipesByRecipeNameAndCuisine")
    public List<Recipe> getRecipesByRecipeNameAndCuisine(@RequestParam String recipeName, @RequestParam Cuisine cuisine){
        return userService.getRecipesByRecipeNameAndCuisine(recipeName,cuisine);
    }

    @GetMapping("recipesByRecipeNameAndSeasoning")
    public List<Recipe> getRecipesByRecipeNameAndSeasoning(@RequestParam String recipeName, @RequestParam Seasoning seasoning){
        return userService.getRecipesByRecipeNameAndSeasoning(recipeName,seasoning);
    }

    @GetMapping("recipesByPrepTimeAndServings")
    public List<Recipe> getRecipesByPrepTimeAndServings(@RequestParam String prepTime, @RequestParam Integer servings){
        return userService.getRecipesByPrepTimeAndServings(prepTime,servings);
    }

    @GetMapping("allRecipesLikedByUser")
    public  List<Recipe> getAllRecipesLikedByUser(@RequestParam String email,@RequestParam String tokenValue){
        return userService.getRecipesLikedByUser(email,tokenValue);
    }

    @GetMapping("allRecipesCommentedByUser")
    public  List<Recipe> getAllRecipesCommentedByUser(@RequestParam String email,@RequestParam String tokenValue){
        return userService.getRecipesCommentedByUser(email,tokenValue);
    }

    @GetMapping("allChefsFollowedByUser")
    public  List<User> getAllChefsFollowedByUser(@RequestParam String email,@RequestParam String tokenValue){
        return userService.getChefsFollowedByUser(email,tokenValue);
    }

    @GetMapping("allRecipesByTags")
    public List<Recipe> getRecipesByTags(@RequestParam String tagName){
        return userService.getRecipesByTags(tagName);
    }

}
