package com.example.RecipeManagementSystem.repo;

import com.example.RecipeManagementSystem.model.Follow;
import com.example.RecipeManagementSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFollowRepo extends JpaRepository<Follow,Integer> {
    List<Follow> findByCurrentUserAndCurrentUserFollower(User target, User follower);

}
