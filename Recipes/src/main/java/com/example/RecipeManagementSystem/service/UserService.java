package com.example.RecipeManagementSystem.service;

import com.example.RecipeManagementSystem.model.*;
import com.example.RecipeManagementSystem.repo.IRecipeRepo;
import com.example.RecipeManagementSystem.repo.IUserRepo;
import com.example.RecipeManagementSystem.service.emailutility.EmailHandler;
import com.example.RecipeManagementSystem.service.hashingutility.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    IUserRepo userRepo;
    @Autowired
    IRecipeRepo recipeRepo;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    RecipeService recipeService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikesService likesService;
    @Autowired
    FollowService followService;


    public String userSignUp(User newUser) {
        String newEmail = newUser.getUserEmail();

        User existingUser = userRepo.findFirstByUserEmail(newEmail);

        if (existingUser != null) {
            return "email already in use";
        }

        // passwords are encrypted before we store it in the table

        String signUpPassword = newUser.getUserPassword();

        try {
            String encryptedPassword = PasswordEncryptor.encrypt(signUpPassword);

            newUser.setUserPassword(encryptedPassword);


            // patient table - save patient
            userRepo.save(newUser);
            return "User Registered";

        } catch (NoSuchAlgorithmException e) {

            return "Internal Server issues while saving password, try again later!!!";
        }
    }

    public String userSignIn(String email, String password) {
        //check if the email is there in your tables
        //sign in only possible if this person ever signed up


        User existingUser = userRepo.findFirstByUserEmail(email);

        if (existingUser == null) {
            return "Not a valid email, Please sign up first !!!";
        }

        //password should be matched

        try {
            String encryptedPassword = PasswordEncryptor.encrypt(password);

            if (existingUser.getUserPassword().equals(encryptedPassword)) {
                // return a token for this sign in
                AuthenticationToken token = new AuthenticationToken(existingUser);

                if (EmailHandler.sendEmail(email, "otp after login", token.getTokenValue())) {
                    authenticationService.createToken(token);
                    return "check email for otp/token!!!";
                } else {
                    return "error while generating token!!!";
                }

            } else {
                //password was wrong!!!
                return "Invalid Credentials!!!";
            }

        } catch (NoSuchAlgorithmException e) {

            return "Internal Server issues while saving password, try again later!!!";
        }
    }


    public String userSignOut(String email, String token) {
        if (authenticationService.authenticate(email, token)) {
            authenticationService.deleteToken(token);
            return "Sign Out successful!!";
        } else {
            return "Un Authenticated Access!!!";
        }
    }

    public String deleteUser(String email, String token) {
        if (authenticationService.authenticate(email, token)) {
            User user = userRepo.findFirstByUserEmail(email);
            Integer userId = user.getUserId();
            userRepo.deleteById(userId);
            return "User Deleted";
        } else
            return "Un Authenticated Access";
    }


    public String createRecipe(String email, String tokenValue, Recipe recipe) {
        if (authenticationService.authenticate(email, tokenValue)) {

            User existingUser = userRepo.findFirstByUserEmail(email);
            recipe.setRecipeOwner(existingUser);

            recipeService.createRecipe(recipe);
            return recipe.getRecipeName() + " Recipe is Posted";
        } else {
            return "Un Authenticated access!!!";
        }
    }


    public String deleteRecipe(String email, String tokenValue, Integer recipeId) {
        if (authenticationService.authenticate(email, tokenValue)) {
            Recipe recipe = recipeService.getRecipeById(recipeId);
            String recipeOwnerEmail = recipe.getRecipeOwner().getUserEmail();

            if (email.equals(recipeOwnerEmail)) {

                //finally delete the recipe
                recipeService.removeById(recipeId);
                return "Recipe Removed!!";

            } else {
                return "Un authorized access!!";
            }


        } else {
            return "Un Authenticated access!!!";
        }
    }


    public String updateRecipe(String email, String tokenValue, Integer recipeId, String time) {
        if (authenticationService.authenticate(email, tokenValue)) {
            Recipe recipe = recipeService.getRecipeById(recipeId);
            String recipeOwnerEmail = recipe.getRecipeOwner().getUserEmail();

            if (email.equals(recipeOwnerEmail)) {

                //finally delete the recipe
                recipeService.updateRecipe(recipeId, time);
                return recipe.getRecipeName() + " Recipe Updated!!";

            } else {
                return "Un authorized access!!";
            }


        } else {
            return "Un Authenticated access!!!";
        }
    }

    public Recipe getRecipe(Integer recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return recipe;
    }


    public String addLike(String email, String tokenValue, Integer recipeId) {
        if (authenticationService.authenticate(email, tokenValue)) {

            //figure out the post which we are liking
            Recipe recipe = recipeService.getRecipeById(recipeId);
            //we have to figure out the liker
            User liker = userRepo.findFirstByUserEmail(email);

            // user cannot like this particular postId more than once

            boolean alreadyLiked = likesService.isAlreadyLiked(recipe, liker);

            if (!alreadyLiked) {
                Likes newLike = new Likes(null, liker, recipe);

                likesService.addLike(newLike);

                return liker.getUserName() + " liked " + recipeService.getRecipeById(recipeId).getRecipeName();
            } else {
                return "You have already liked the recipe" + recipe.getRecipeName();
            }
        } else {
            return "Un Authenticated access!!!";
        }
    }


    public String removeLike(String email, String tokenValue, Integer recipeId) {
        if (authenticationService.authenticate(email, tokenValue)) {

            User potentialLiker = userRepo.findFirstByUserEmail(email);

            Recipe recipeToBeUnLiked = recipeService.getRecipeById(recipeId);

            return likesService.removeLikeByLikerAndRecipe(potentialLiker, recipeToBeUnLiked);

        } else {
            return "Un Authenticated access!!!";
        }
    }

    public String addComment(String email, String tokenValue, String commentBody, Integer recipeId) {
        if (authenticationService.authenticate(email, tokenValue)) {

            //figure out the recipe which we are commenting
            Recipe recipe = recipeService.getRecipeById(recipeId);
            //we have to figure out the commenter
            User commenter = userRepo.findFirstByUserEmail(email);

            // functionally more than 1 comment can be made on a particular recipe

            Comment newComment = new Comment(null, commentBody,
                    LocalDateTime.now(), recipe, commenter);

            commentService.addComment(newComment);

            return commenter.getUserName() + " commented on " + recipeService.getRecipeById(recipeId).getRecipeName();
        } else {
            return "Un Authenticated access!!!";
        }
    }


    public String removeComment(String email, String tokenValue, Integer commentId) {
        if (authenticationService.authenticate(email, tokenValue)) {
            Comment comment = commentService.findCommentById(commentId);

            Recipe recipe = comment.getRecipe();

            if (authorizeCommentRemover(email, recipe, comment)) {
                commentService.removeCommentById(commentId);
                return "comment deleted";
            } else {
                return "Not authorized!!";
            }

        } else {
            return "Un Authenticated access!!!";
        }
    }

    private boolean authorizeCommentRemover(String email, Recipe recipe, Comment comment) {
        User potentialRemover = userRepo.findFirstByUserEmail(email);

        //only the commenter and the owner of the recipe should be allowed to remove a comment

        return potentialRemover.equals(recipe.getRecipeOwner()) || potentialRemover.equals(comment.getCommenter());
    }

    public String followTarget(String email, String tokenValue, Integer targetUserId) {
        if (authenticationService.authenticate(email, tokenValue)) {

            User follower = userRepo.findFirstByUserEmail(email);
            User target = userRepo.findById(targetUserId).orElseThrow();

            if (authorizeToFollow(follower, target)) {
                followService.startFollowing(follower, target);
                return follower.getUserName() + " started following " + target.getUserName();
            } else {
                return "Already follows, cannot re-follow";
            }

        } else {
            return "Un Authenticated access!!!";
        }
    }

    private boolean authorizeToFollow(User follower, User target) {
        boolean followingExist = followService.findByTargetAndFollower(follower, target);

        return !followingExist && !follower.equals(target);
    }

    public String unFollowTarget(String email, String tokenValue, Integer targetUserId, Integer followId) {
        User follower = userRepo.findFirstByUserEmail(email);
        User target = userRepo.findById(targetUserId).orElseThrow();
        if (authenticationService.authenticate(email, tokenValue)) {
            if (followService.findByTargetAndFollower(follower, target)) {
                followService.removeFollower(followId);
                return "UnFollowed";
            } else {
                return "You are not following the user";
            }
        } else
            return "Un Authenticated access!!!";
    }

    public List<Recipe> getRecipesByDate(String email, String tokenValue, LocalDate date) {
        List<Recipe> recipes = new ArrayList<>();
        if (authenticationService.authenticate(email, tokenValue)) {
            for (Recipe recipe : recipeRepo.findAll()) {
                if (recipe.getRecipeOwner().getUserEmail().equals(email)) {
                    if (recipe.getCreatedDate().equals(date)) {
                        recipes.add(recipe);
                    }
                }
            }
        }
        return recipes;
    }

    public List<Recipe> getRecipesByChefsName(String email, String tokenValue, String name) {
        List<Recipe> recipes = new ArrayList<>();
        if (authenticationService.authenticate(email, tokenValue)) {
            for (Recipe recipe : recipeRepo.findAll()) {
                if (recipe.getRecipeOwner().getUserName().equals(name)) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }
    // to  get recipes by name
    public List<Recipe> getRecipesByName(String email, String tokenValue, String recipeName) {
        List<Recipe> recipes = new ArrayList<>();
        if (authenticationService.authenticate(email, tokenValue)) {
            for (Recipe recipe : recipeRepo.findAll()) {
                if (recipe.getRecipeName().equals(recipeName)) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    public List<Recipe> getRecipesByCategory(String email, String tokenValue, Categories category) {
        List<Recipe> recipes = new ArrayList<>();
        if (authenticationService.authenticate(email, tokenValue)) {
            for (Recipe recipe : recipeRepo.findAll()) {
                if (recipe.getRecipeCategory().equals(category)) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    public List<Recipe> getRecipesByCuisine(String email, String tokenValue, Cuisine cuisine) {
        List<Recipe> recipes = new ArrayList<>();
        if (authenticationService.authenticate(email, tokenValue)) {
            for (Recipe recipe : recipeRepo.findAll()) {
                if (recipe.getCuisine().equals(cuisine)) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    // to get recipes by format
    public List<Recipe> getRecipesByFormat(String email, String tokenValue, RecipeFormat recipeFormat) {
        List<Recipe> recipes = new ArrayList<>();
        if (authenticationService.authenticate(email, tokenValue)) {
            for (Recipe recipe : recipeRepo.findAll()) {
                if (recipe.getRecipeFormat().equals(recipeFormat)) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }


    public List<Recipe> getRecipesByTime(String email, String tokenValue, String time) {
        List<Recipe> recipes = new ArrayList<>();
        if (authenticationService.authenticate(email, tokenValue)) {
            for (Recipe recipe : recipeRepo.findAll()) {
                if (recipe.getPreparationTime().equals(time)) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }


    public List<Recipe> getRecipesByChefsNameAndRecipeName(String email, String tokenValue, String chefsName, String recipeName) {
        List<Recipe> recipesByChef = getRecipesByChefsName(email, tokenValue, chefsName);
        List<Recipe> recipes = new ArrayList<>();
        if (authenticationService.authenticate(email, tokenValue)) {
            for (Recipe recipe : recipesByChef) {
                if (recipe.getRecipeName().equals(recipeName)) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }


    public List<Recipe> getRecipesByRecipeNameAndFormat(String recipeName, RecipeFormat format) {
        return recipeRepo.findByRecipeNameAndRecipeFormat(recipeName, format);
    }

    public List<Recipe> getRecipesByRecipeNameAndCuisine(String recipeName, Cuisine cuisine) {
        return recipeRepo.findByRecipeNameAndCuisine(recipeName, cuisine);
    }

    public List<Recipe> getRecipesByRecipeNameAndSeasoning(String recipeName, Seasoning seasoning) {
        return recipeRepo.findByRecipeNameAndSeasoning(recipeName, seasoning);
    }

    public List<Recipe> getRecipesByPrepTimeAndServings(String prepTime, Integer servings) {
        return recipeRepo.findByPreparationTimeAndServings(prepTime, servings);
    }

    public List<Recipe> getRecipesLikedByUser(String email, String tokenValue) {
        List<Recipe> likedRecipes = new ArrayList<>();
        User liker = userRepo.findFirstByUserEmail(email);
        if (authenticationService.authenticate(email, tokenValue)) {
            for (Recipe recipe : recipeRepo.findAll()) {
                boolean alreadyLiked = likesService.isAlreadyLiked(recipe, liker);
                if (alreadyLiked) {
                    likedRecipes.add(recipe);
                }
            }
        }
        return likedRecipes;
    }

    public List<Recipe> getRecipesCommentedByUser(String email,String tokenValue){
        List<Recipe> commentedRecipes = new ArrayList<>();
        User commenter = userRepo.findFirstByUserEmail(email);
        if (authenticationService.authenticate(email, tokenValue)) {
            for (Recipe recipe : recipeRepo.findAll()) {
                boolean commentedRecipe = commentService.isCommented(recipe,commenter);
                if (commentedRecipe) {
                    commentedRecipes.add(recipe);
                }
            }
        }
        return commentedRecipes;
    }

    public List<User> getChefsFollowedByUser(String email,String tokenValue){
        List<User> followingChefs = new ArrayList<>();
        User follower = userRepo.findFirstByUserEmail(email);
        if (authenticationService.authenticate(email, tokenValue)) {
            for (User chef : userRepo.findAll()) {
                if (followService.findByTargetAndFollower(follower, chef)) {
                    followingChefs.add(chef);
                }
            }
        }
        return followingChefs;
    }

    public List<Recipe> getRecipesByTags(String tagName){
        return recipeRepo.findByTags(tagName);
    }

}
