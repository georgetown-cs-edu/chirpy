package edu.georgetown.chirpy.bll.user;

import java.util.logging.Logger;
import edu.georgetown.chirpy.dao.*;

public class UserService {

    private Logger logger;

    public UserService( Logger logger ) {
        this.logger = logger;
        Users users = new Users( logger );
        this.logger.info( "UserService created; reading user file" );
        users.ReadUsers();
    }

}