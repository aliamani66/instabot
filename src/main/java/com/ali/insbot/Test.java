package com.ali.insbot;


import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramTagFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.*;

import java.io.IOException;
import java.util.*;

/**
     * Created by ali.amani on 8/4/2019.
     */
    public class Test {

        private static Map<String,String> users= new HashMap<>();

        public static void main(String[] args) throws IOException {
            users.put("alitest5", "aliamani");
            users.put("shoghlyabi","qweszxc@1");
            users.put("abzar.foroshi","qweszxc");
            users.put("arayesh.jigh","qweszxc");
            users.put("omde.forosh","qweszxc");
            List<Instagram4j> instagrams = new ArrayList<>();

            List<UserSendRequest> sendersReques = new ArrayList<>();
            for (String user : users.keySet()) {
                Instagram4j instagram = Instagram4j.builder().username(user).password(users.get(user)).build();
                instagram.setup();
                instagram.login();
                instagrams.add(instagram);
                UserSendRequest userSendRequest = new UserSendRequest(user ,instagram);
                sendersReques.add(userSendRequest);
            }

            InstagramSearchUsernameResult userResult = instagrams.get(0).sendRequest(new InstagramSearchUsernameRequest("havashi_clip1"));
            System.out.println("ID for aria_cosmetic is " + userResult.getUser().getPk());
            System.out.println("Number of followers: " + userResult.getUser().getFollower_count());
            InstagramGetUserFollowersResult githubFollowers =
                    instagrams.get(0).sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));
            long count=0;

            while(count < userResult.getUser().getFollower_count()) {

               List<InstagramUserSummary> users = githubFollowers.getUsers();
               System.out.println(users.size());
               for (InstagramUserSummary user : users) {
                   System.out.println(count + " = User " + user.getUsername() + " follows me!");

                   for (UserSendRequest sender : sendersReques){
                       sender.addRequestForRun(new InstagramFollowRequest(user.getPk()));
                   }
                   count+=1;

                   if(count == 1000){
                       for (UserSendRequest sender : sendersReques){
                           Thread thread = new Thread(sender);
                           thread.start();
                       }
                   }
               }
               githubFollowers = instagrams.get(0).sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk(), githubFollowers.getNext_max_id()));
            }
        }

    }