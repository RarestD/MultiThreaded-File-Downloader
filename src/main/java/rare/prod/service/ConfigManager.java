package rare.prod.service;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties props = new Properties();

    static {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getTargetFolder() {
        return props.getProperty("targetFolder");
    }

    public static void setTargetFolder(String folderPath) {
        props.setProperty("targetFolder", folderPath);
        save();
    }

    private static void save() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "Downloader Config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

