package server.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.SelfLogger;

public class PluginLoader {

    public File plugins;
    public HashMap<String, DefaultPlugin> pluginObjectMap = new HashMap<>();

    public PluginLoader(File plugins) {
        this.plugins = plugins;
        if (!this.plugins.exists()) {
            this.plugins.mkdir();
        }
    }

    public void loadPlugins() {
        for (File plugin : plugins.listFiles()) {
            if (!plugin.isDirectory()) {
                loadPlugin(plugin);
            }
        }
    }

    public void loadPlugin(File plugin) {
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException | SecurityException e1) {
        }
        boolean accessible = method.isAccessible();
        try {
            method.setAccessible(true);
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            URL url = plugin.toURI().toURL();
            method.invoke(classLoader, url);
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException | MalformedURLException e) {
        } finally {
            method.setAccessible(accessible);
        }
        HashMap<String, String> info = getPluginInfo(plugin);
        SelfLogger.info("[" + info.get("name") + "] " + info.get("name") + " V" + info.get("version") + " is enabled");
        try {
            DefaultPlugin javaPlugin = (DefaultPlugin) create(Class.forName(info.get("main")));
            javaPlugin.name = info.get("name");
            pluginObjectMap.put(info.get("name"), javaPlugin);
            doMethod(javaPlugin, "onEnable");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<String, String> getPluginInfo(File file) {
        HashMap<String, String> info = new HashMap<>();
        try {
            JarFile jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("plugin.yml");
            InputStream inputStream = jar.getInputStream(entry);
            String str = inputStreamToString(inputStream);
            for (String line : str.split("\n")) {
                if (line.split(": ").length == 2) {
                    String key = line.split(": ")[0];
                    String value = line.split(": ")[1];
                    info.put(key, value);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return info;
    }

    public String inputStreamToString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(PluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    public static Object create(Class<?> clazz, ParamGroup... args) {
        try {
            Class<?>[] types = new Class<?>[args.length];
            Object[] objs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].type;
                objs[i] = args[i].obj;
            }
            Constructor<?> cons = clazz.getConstructor(types);
            return cons.newInstance(objs);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
        return null;
    }

    public static Object doMethod(Object obj, String methodName, ParamGroup... args) {
        try {
            Class<?>[] types = new Class<?>[args.length];
            Object[] objs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].type;
                objs[i] = args[i].obj;
            }
            Method method = obj.getClass().getMethod(methodName, types);
            return method.invoke(obj, objs);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
        return null;
    }

    public static Object doStaticMethod(Class<?> clazz, String methodName, ParamGroup... args) {
        try {
            Class<?>[] types = new Class<?>[args.length];
            Object[] objs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].type;
                objs[i] = args[i].obj;
            }
            Method method = clazz.getMethod(methodName, types);
            return method.invoke(null, objs);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
        return null;
    }

    public static class ParamGroup {

        public Object obj;
        public Class<?> type;

        public ParamGroup(Object obj, Class<?> type) {
            this.obj = obj;
            this.type = type;
        }

        public ParamGroup(Object obj) {
            this.obj = obj;
            this.type = obj.getClass();
        }
    }
}
