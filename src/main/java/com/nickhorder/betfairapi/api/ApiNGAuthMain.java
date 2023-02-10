package com.nickhorder.betfairapi.api;

import com.nickhorder.betfairapi.entities.Login;
import com.nickhorder.betfairapi.exceptions.APINGException;

import java.io.*;
import java.text.ParseException;
import java.util.Properties;


/**
  Some comments here
 *
 */

public class ApiNGAuthMain {

     private static Properties prop = new Properties();
     private static String applicationKey;
     private static String sessionToken;
     private static String username;
     private static String password;
     private static String keyStorePassword;
     private static boolean debug;
     private static int port = 443;

     public static Properties getProp() {
         return prop;
     }

     public static boolean isDebug() {
         return debug;
     }


     static {
         try {
             InputStream in = ApiNGAuthMain.class.getResourceAsStream("/apingdemo.properties");
             prop.load(in);
             in.close();

             debug = Boolean.parseBoolean(prop.getProperty("DEBUG"));

         } catch (IOException e) {
             System.out.println("Error loading the properties file: " + e);
         }
     }


     public static void main(String[] args) throws APINGException, ParseException, IOException {


         // Check whether the program can start at the time the user has requested.

         if (ProgramPause.programStartStop(true) == true)
         {
             System.out.println("Welcome to the Betfair API NG!. This program runs within an operating" +
                     " window of "+ ProgramPause.getStartTimeInConsole() + " to "
                     + ProgramPause.getEndTimeInConsole() + ".");
         }
         else{
             System.out.println("This program can be started at or after " + ProgramPause.getStartTimeInConsole() +
              " .Please return after that time.");
         }

         // Check if we have all args needed for automatic login (appkey,username,password).
         // And that the program is being started in an acceptable time range.
         if (args.length >= 3 && ProgramPause.getProgramRunning()) {

             applicationKey = args[0];
             username = args[1];
             password = args[2];
             keyStorePassword = args[3];

             System.out.println("Conducting automatic login.");

             //Call authCall method to login via Keystore.
             HttpAuth authCall = new HttpAuth();
             Login newLogin = authCall.sendAuthRequest(port, applicationKey, username, password, keyStorePassword);
             if(newLogin.getLoginStatus().equals("SUCCESS")) {

                 sessionToken = newLogin.getSessionToken();
                 System.out.println("Your session token is: " + sessionToken);
             }
             else{
                  System.out.println("Call to obtain Session Token Failed.");
             }

             // POST-AUTHENTICATION PROGRAM FLOW STARTS HERE //

             //call instance of ApiNGFlowController with sessionToken
             ApiNGFlowController goBetting = new ApiNGFlowController();
             goBetting.runAPIs(applicationKey, sessionToken);


             }
         }
     }
