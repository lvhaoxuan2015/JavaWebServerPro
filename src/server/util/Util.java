package server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    public static String hash(String password) {
        String ret = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((password + "edaur12!*(&E&*!kjgda¡·£¿£º¡°jrhgea").getBytes());
            byte s[] = md.digest();
            for (int i = 0; i < s.length; i++) {
                ret += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
}
