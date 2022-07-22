package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Conf {

    private static volatile Conf conf;
    private HashMap<String, String> confs;

    private Conf() {
        confs = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/xtend/IdeaProjects/EchoServer/src/main/resources/app.config"))){
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                String key = line.substring(0, line.indexOf('='));
                String value = line.substring(line.indexOf('=') + 1, line.length());
                confs.put(key, value);
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Conf GetInstance(){
        Conf localInstance = conf;
        if (localInstance == null) {
            synchronized (Conf.class) {
                localInstance = conf;
                if (localInstance == null) {
                    conf = localInstance = new Conf();
                }
            }
        }
        return localInstance;
    }

    public Map<String, String> GetConfs() {
        return confs;
    }
}
