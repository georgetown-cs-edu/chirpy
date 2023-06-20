/**
 * Storage layer for chirpy
 * 
 * Micah Sherr <msherr@cs.georgetown.edu>
 */

package edu.georgetown.chirpy.dao;

public class User {
    
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
        Users.getLogger().debug( "Adding follower " + follower.getUsername() + " to " + this.getUsername() );
        this.followers.add( follower );
    }

    public void getFollowers() {
        return this.followers;
    }

}