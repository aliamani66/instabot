package com.ali.insbot;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

import java.io.IOException;
import java.util.List;

public class InstaLogin {

    public static void main(String[] args) {
        // Login to instagram
        Instagram4j instagram = Instagram4j.builder().username("alitest5").password("qweszxc@1").build();
        instagram.setup();
        try {
            instagram.login();
            InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest("royal.cosmetics.original"));
            System.out.println("ID for aria_cosmetic is " + userResult.getUser().getPk());
            System.out.println("Number of followers: " + userResult.getUser().getFollower_count());

            InstagramGetUserFollowersResult githubFollowers =
                    instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));
            long count=0;
            StatusResult result;
            while(count < userResult.getUser().getFollower_count()) {

               List<InstagramUserSummary> users = githubFollowers.getUsers();
               System.out.println(users.size());
               for (InstagramUserSummary user : users) {
                   System.out.println(count + " = User " + user.getUsername() + " follows me!");
                   result = instagram.sendRequest(new InstagramFollowRequest(user.getPk()));
                   Thread.sleep(20000);
                   if(result.getStatus().equals("fail")){
                       break;
                   }
                   count+=1;
               }

               githubFollowers = instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk(), githubFollowers.getNext_max_id()));

            }

/*
            InstagramGetUserFollowersResult githubFollowing =
                    instagram.sendRequest(new InstagramGetUserFollowingRequest(userResult.getUser().getPk()));
            long count=0;
            while(count < userResult.getUser().getFollower_count()) {

                List<InstagramUserSummary> users = githubFollowers.getUsers();
                System.out.println(users.size());
                for (InstagramUserSummary user : users) {
                    System.out.println(count + " = User " + user.getUsername() + " follows me!");
                    instagram.sendRequest(new InstagramFollowRequest(user.getPk()));
                    count+=1;
                }

                githubFollowers = instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk(), githubFollowers.getNext_max_id()));

            }*/

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
