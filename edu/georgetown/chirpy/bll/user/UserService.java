package edu.georgetown.chirpy.bll.user;

import edu.georgetown.chirpy.dao.Users;
import java.util.logging.Logger;

class UserService {

    private Logger logger;

    public UserService( Logger logger ) {
        this.logger = logger;
        users = new edu.georgetown.chirpy.dao.Users();
        this.logger.debug( "UserService created; reading user file" );
        users.ReadUsers();
    }

}