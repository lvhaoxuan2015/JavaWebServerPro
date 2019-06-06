package fileshare.user;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.util.Util;

public class User {

    private final String name;
    private final String password;
    private final String cockie;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.cockie = Util.hash(name + password);
    }

    public User(String name, String password, String cockie) {
        this.name = name;
        this.password = password;
        this.cockie = cockie;
    }

    public String getName() {
        try {
            return URLDecoder.decode(name, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getCockie() {
        return cockie;
    }

    public String getPassword() {
        return password;
    }
}
