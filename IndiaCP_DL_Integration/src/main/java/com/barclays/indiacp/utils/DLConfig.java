package com.barclays.indiacp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by ritukedia on 22/12/16.
 */
public class DLConfig extends Properties {
    private static DLConfig dlConfig;

    private String DL;

    private DLConfig() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("DL_CONFIG.properties").getFile());
            FileInputStream input = new FileInputStream(file);
            load(input);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static DLConfig DLConfigInstance() {
        if (dlConfig == null) {
            dlConfig = new DLConfig();
        }
        return dlConfig;
    }

    public String getDLRestEndpoint() {
        DL = getProperty("DL");

        String scheme = getDLProperty("scheme");
        String webHost = getDLProperty("webhost");
        String webPort = getDLProperty("webport");
        String basepath = getDLProperty("basepath");

        return scheme + "://" + webHost + ":" + webPort + "/" + basepath + "/";
    }

    public String getDLProperty(String property) {
        return getProperty(DL + "." + property);
    }
}
