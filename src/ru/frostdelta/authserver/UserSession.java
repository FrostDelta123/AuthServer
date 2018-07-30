package ru.frostdelta.authserver;

import java.util.HashMap;

public class UserSession {

    public static HashMap<String, Boolean> session = new HashMap<String, Boolean>();


    public UserSession(String username) {

        UserSession.session.put(username, true);

    }

    public UserSession() {

    }


    public boolean usersession(String username){

        if(session.containsKey(username)){

            return true;

        }else return false;
    }

}
