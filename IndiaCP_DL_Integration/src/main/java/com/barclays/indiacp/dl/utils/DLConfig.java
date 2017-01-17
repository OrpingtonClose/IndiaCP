package com.barclays.indiacp.dl.utils;

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
            String dlConfigFilePath = System.getProperty("DL_CONFIG_FILE");
            if (dlConfigFilePath == null) {
                ClassLoader classLoader = getClass().getClassLoader();
                dlConfigFilePath = classLoader.getResource("DL_CONFIG_TEST.properties").getFile();
            }
            File file = new File(dlConfigFilePath);
            FileInputStream input = new FileInputStream(file);
            load(input);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
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

    public String getDLAttachmentRestEndpoint() {
        DL = getProperty("DL");

        String scheme = getDLProperty("scheme");
        String webHost = getDLProperty("webhost");
        String webPort = getDLProperty("webport");

        return scheme + "://" + webHost + ":" + webPort + "/";
    }

    public String getDLUploadAttachmentPath() {
        return getDLProperty("uploadAttachmentPath");
    }

    public String getDLDownloadAttachmentPath() {
        return getDLProperty("downloadAttachmentPath");
    }

    public String getDLProperty(String property) {

        if (DL == null) {
            DL = getProperty("DL");
        }

        return getProperty(DL + "." + property);

    }
}
