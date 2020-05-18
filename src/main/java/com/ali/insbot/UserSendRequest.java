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
import java.util.logging.Logger;

/**
 * Created by ali.amani on 8/4/2019.
 */
public class UserSendRequest implements Runnable{

    private static long reqPerHour = 200l;
    static Logger logger = Logger.getLogger(UserSendRequest.class.getName());
    String user;
    Instagram4j instagram;
//    List<InstagramRequest> instagramRequest = Collections.synchronizedList(new ArrayList<>());
    List<InstagramRequest> instagramRequest = new CopyOnWriteArrayList();
    Map<InstagramRequest, StatusResult> resultMap = new HashMap<>();
    private volatile boolean running = true;
    private int errorCount=0;
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

    public void terminate(){
        running = false;
    }
    @Override
    public void run() {

        while (running) {
            for (InstagramRequest req : instagramRequest) {
                try {

                    StatusResult userResult = (StatusResult) instagram.sendRequest(req);
                    logger.info("logger in "+ this.user +" === send request for " + this.user + " " + userResult.toString());
                    if (!userResult.getStatus().equals("ok")) {
                        errorCount ++;
                    }
                    resultMap.put(req, (StatusResult) userResult);

                    Thread.sleep(HumanSimulatorTime.generateNextInterval());

                    if(errorCount > 5){
                        running = false;
                    }

                } catch (IOException e) {
                    resultMap.put(req, new StatusResult(e.getMessage()));
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
