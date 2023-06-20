

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

    public Users( Logger log) {
        logger = log;
        this.users = new Vector<User>();
    }

    public static Logger getLogger() {
        return logger;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void ReadUsers() {
        try {
            FileInputStream fileIn = new FileOutputStream("/tmp/users.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.users = in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            throw new RuntimeException("Could not read users");
        }
        logger.debug("Read " + this.users.size() + " users");
    }

    /**
     * Saves users to a file
     */
    public void saveUsers() {
        try {
            FileOutputStream fileOut = new FileOutputStream("/tmp/users.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(users);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
            throw new RuntimeException("Could not save users");
        }
        logger.debug("Saved " + this.users.size() + " users");
    }
}