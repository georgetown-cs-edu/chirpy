/**
 * Storage layer for chirpy
 * 
 * Micah Sherr <msherr@cs.georgetown.edu>
 */

package edu.georgetown.chirpy.dev.src.main.java.edu.georgetown.chirpy.dao;

import java.io.Serializable;
import java.util.Vector;

public class User implements Serializable {
    
    private String username;
    private String name;
    private String password;

    private Vector<User> followers;


    public User( String username, String name, String password ) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.followers = new Vector<User>();        
    }

    /**
     * Gets the user's username
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    public String getName() {
        return this.name;
    }

    public boolean checkPassword( String password ) {
        return this.password.equals( password );
    }

    public void addFollower( User follower ) {
        Users.getLogger().info( "Adding follower " + follower.getUsername() + " to " + this.getUsername() );
        this.followers.add( follower );
    }

    public Vector<User> getFollowers() {
        return this.followers;
    }

}