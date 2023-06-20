package edu.georgetown.chirpy.bll.user;

import java.util.logging.Logger;

import Users;

public class UserService {

    private Logger logger;

    public UserService( Logger logger ) {
        this.logger = logger;
        Users users = new Users();
        this.logger.debug( "UserService created; reading user file" );
        users.ReadUsers();
    }

}