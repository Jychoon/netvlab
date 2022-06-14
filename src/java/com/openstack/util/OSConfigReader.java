package com.openstack.util;

import com.opensymphony.xwork2.util.PropertiesReader;
import java.io.*;
import java.util.Properties;
import java.io.IOException;

public final class OSConfigReader {
    private static Properties config = new Properties();
    static {
        try{
            InputStream in = PropertiesReader.class.getResourceAsStream("/opentack.properties");
            config.load(in);
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getValue(String key)
    {
        if(config.containsKey(key))
            return config.getProperty(key);
        else
            return null;
    }

    public static boolean setValue(String key,String value){
        if(config.containsKey(key)){
            config.setProperty(key, value);
            return true;
        }
        return false;
    }
}
