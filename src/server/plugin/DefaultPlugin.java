package server.plugin;

import java.io.File;

public class DefaultPlugin {

    public String name;

    public void onEnable() {
    }

    public File getDataFolder() {
        return new File(new File(System.getProperty("user.dir"), "plugins"), name);
    }
}
