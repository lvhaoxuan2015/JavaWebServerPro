package fileshare.user;

import fileshare.FileShare;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.API;

public class UserManager {

    public static HashMap<String, User> users = new HashMap<>();
    public static HashMap<String, User> cockieUsers = new HashMap<>();
    public static File save;

    public static void init() {
        save = new File(FileShare.ins.getDataFolder(), "users");
        if (!save.exists()) {
            save.mkdir();
        }
        for (File file : save.listFiles()) {
            String data = API.readToString(file);
            String[] userData = data.split("\n");
            User user = new User(userData[0], userData[1]);
            adminRegister(user);
        }
    }

    public static boolean login(User user) {
        return users.containsKey(user.getName()) && users.get(user.getName()).getPassword().equals(user.getPassword());
    }

    public static void adminRegister(User user) {
        users.put(user.getName(), user);
        cockieUsers.put(user.getCockie(), user);
    }

    public static boolean register(User user) {
        if (!users.containsKey(user.getName())) {
            users.put(user.getName(), user);
            cockieUsers.put(user.getCockie(), user);
            File file = new File(save, user.getCockie());
            try {
                file.createNewFile();
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write((user.getName() + "\n" + user.getPassword()).getBytes());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        return false;
    }
}
