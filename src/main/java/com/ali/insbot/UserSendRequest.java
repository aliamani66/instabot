package com.ali.insbot;


import jdk.net.SocketFlow;
import org.brunocvcunha.instagram4j.*;
import org.brunocvcunha.instagram4j.requests.*;
import org.brunocvcunha.instagram4j.requests.InstagramRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ali.amani on 8/4/2019.
 */
public class UserSendRequest implements Runnable{

    private static long reqPerHour = 200l;

    String user;
    Instagram4j instagram;
//    List<InstagramRequest> instagramRequest = Collections.synchronizedList(new ArrayList<>());
    List<InstagramRequest> instagramRequest = new CopyOnWriteArrayList();
    Map<InstagramRequest, StatusResult> resultMap = new HashMap<>();

    public UserSendRequest(String user, InstagramRequest instagramRequest,Instagram4j instagram4j){
        this.instagramRequest.add(instagramRequest);
        this.user = user;
        this.instagram = instagram4j;
    }

    public UserSendRequest(String user, Instagram4j instagram4j){
        this.user = user;
        this.instagram = instagram4j;
    }


    public void addRequestForRun(InstagramRequest instagramRequest){
        this.instagramRequest.add(instagramRequest);
    }

    @Override
    public void run() {

        for (InstagramRequest req : instagramRequest) {

            try {

                StatusResult userResult = (StatusResult) instagram.sendRequest(req);
                System.out.println("send request for "+ this.user +" " +userResult.toString());
                if(!userResult.getStatus().equals("ok")){
                    Thread.sleep(3600000);
                }
                resultMap.put(req, (StatusResult) userResult);
                Thread.sleep(50000);

            } catch (IOException e) {
                resultMap.put(req, new StatusResult(e.getMessage()));
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }



}
