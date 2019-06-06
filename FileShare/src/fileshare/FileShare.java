package fileshare;

import fileshare.event.HTTPConnectEventExtends;
import fileshare.user.UserManager;
import server.JWSPro;
import server.plugin.DefaultPlugin;

public class FileShare extends DefaultPlugin {

    public static FileShare ins;

    @Override
    public void onEnable() {
        ins = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        UserManager.init();
        HTTPConnectEventExtends event = new HTTPConnectEventExtends();
        JWSPro.webServer.JWSPeventManager.registerEvent(event);
    }

}
