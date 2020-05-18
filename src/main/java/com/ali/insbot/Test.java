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
            users.put("donyaedastsaze", "3307874");
            /*users.put("shoghlyabi","qweszxc@1");
            users.put("abzar.foroshi","qweszxc");
            users.put("arayesh.jigh","qweszxc");
            users.put("omde.forosh","qweszxc");*/
            List<Instagram4j> instagrams = new ArrayList<>();

            List<UserSendRequest> sendersRequestUsers = new ArrayList<>();
            for (String user : users.keySet()) {
                Instagram4j instagram = Instagram4j.builder().username(user).password(users.get(user)).build();
                instagram.setup();
                instagram.login();
                instagrams.add(instagram);
                UserSendRequest userSendRequest = new UserSendRequest(user ,instagram);
                sendersRequestUsers.add(userSendRequest);
            }

            InstagramSearchUsernameResult targetPageForFollowers = instagrams.get(0).sendRequest(new InstagramSearchUsernameRequest("javaherdozi.needle"));
            System.out.println("ID for aria_cosmetic is " + targetPageForFollowers.getUser().getPk());
            System.out.println("Number of followers: " + targetPageForFollowers.getUser().getFollower_count());
            InstagramGetUserFollowersResult targetPageFollowers =
                    instagrams.get(0).sendRequest(new InstagramGetUserFollowersRequest(targetPageForFollowers.getUser().getPk()));
            long count=0;

            while(count < targetPageForFollowers.getUser().getFollower_count()) {

               List<InstagramUserSummary> targetPageFollowerUsers = targetPageFollowers.getUsers();
               System.out.println(targetPageFollowerUsers.size());
               for (InstagramUserSummary followersUsersuser : targetPageFollowerUsers) {
                   System.out.println(count + " = User " + followersUsersuser.getUsername() + " follows me!");

                   for (UserSendRequest sender : sendersRequestUsers){
                       sender.addRequestForRun(new InstagramFollowRequest(followersUsersuser.getPk()));
                   }
                   count+=1;

/*
                   if(count == 1000){
                       for (UserSendRequest sender : sendersRequestUsers){
                           Thread thread = new Thread(sender);
                           thread.start();
                       }
                   }
*/

               }
               targetPageFollowers = instagrams.get(0).sendRequest(new InstagramGetUserFollowersRequest(targetPageForFollowers.getUser().getPk(), targetPageFollowers.getNext_max_id()));
            }

            for (UserSendRequest sender : sendersRequestUsers){
                Thread thread = new Thread(sender);
                thread.start();
            }
        }

    }