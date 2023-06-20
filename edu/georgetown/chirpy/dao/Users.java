package edu.georgetown.chirpy.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.logging.Logger;

public class Users {
    protected static Logger logger;
    private Vector<User> users;
    private static final String USERS_SERIALIZED_FILE_PATH = "/tmp/users.ser";

    public Users(Logger log) {
        logger = log;
        this.users = new Vector<User>();
        File f = new File(USERS_SERIALIZED_FILE_PATH);
        if (!f.exists()) {
            saveUsers();
        }

    }

    public static Logger getLogger() {
        return logger;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void ReadUsers() {
        try {
            FileInputStream fileIn = new FileInputStream(USERS_SERIALIZED_FILE_PATH);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.users = (Vector<User>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            throw new RuntimeException("Could not read users");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not read users");
        }
        logger.info("Read " + this.users.size() + " users");
    }

    /**
     * Saves users to a file
     */
    public void saveUsers() {
        try {
            FileOutputStream fileOut = new FileOutputStream(USERS_SERIALIZED_FILE_PATH);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(users);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
            throw new RuntimeException("Could not save users");
        }
        logger.info("Saved " + this.users.size() + " users");
    }
}