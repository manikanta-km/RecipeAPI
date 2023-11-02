package com.example.RecipeManagementSystem.service;

import com.example.RecipeManagementSystem.model.Follow;
import com.example.RecipeManagementSystem.model.User;
import com.example.RecipeManagementSystem.repo.IFollowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    @Autowired
    IFollowRepo followRepo;
    public boolean findByTargetAndFollower(User follower, User target) {
        List<Follow> follows =  followRepo.findByCurrentUserAndCurrentUserFollower(target,follower);

        return !follows.isEmpty();
    }

    public void startFollowing(User follower, User target) {
        Follow follow = new Follow(null,target,follower);
        followRepo.save(follow);
    }

    public void removeFollower(Integer followId) {
        followRepo.deleteById(followId);
    }

}
