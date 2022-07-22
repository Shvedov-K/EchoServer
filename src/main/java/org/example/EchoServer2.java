package org.example;



import org.apache.log4j.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class EchoServer2 extends Thread {
    protected Socket clientSocket;
    private static Conf conf = Conf.GetInstance();
    static boolean debug = Boolean.parseBoolean(conf.GetConfs().get("debug"));
    private static
    ConcurrentHashMap<String, RequestCounter> requestCounterConcurrentHashMap = new ConcurrentHashMap<>();

    private static final Logger log = Logger.getLogger(EchoServer2.class);

    public static void main(String[] args) throws InterruptedException {
        ServerSocket serverSocket = null;
        int listeningPort = Integer.parseInt(conf.GetConfs().get("listening_port"));

        try {
            serverSocket = new ServerSocket(listeningPort);
            if(debug) log.debug ("Connection Socket Created");
            try {
                while (true)
                {
                    if(debug) log.debug ("Waiting for Connection");
                    new EchoServer2 (serverSocket.accept());
                }
            }
            catch (IOException e)
            {
                if(debug) log.error ("Accept failed.");
                System.exit(1);
            }
        }
        catch (IOException e)
        {
            if(debug) log.error ("Could not listen on port: " + listeningPort);
            System.exit(1);
        }
        finally
        {
            try {
                serverSocket.close();
            }
            catch (IOException e)
            {
                if(debug) log.error ("Could not close port: " + listeningPort);
                System.exit(1);
            }
        }
    }

    private EchoServer2 (Socket clientSoc)
    {
        clientSocket = clientSoc;
        start();
    }

    public void run()
    {
        if(debug) log.debug ("New Communication Thread Started");

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));

            String inputLine;
            String name = in.readLine();
            if(debug) log.debug ("Name: " + name);
            out.println(name);
            if (!requestCounterConcurrentHashMap.containsKey(name)) {
                requestCounterConcurrentHashMap.put(name, new RequestCounter());
            }


            while ((inputLine = in.readLine()) != null) {
                if(debug) log.debug ("Server: " + inputLine);

                if (inputLine.equals(":q")) {
                    break;
                }

                RequestCounter requestCounter = requestCounterConcurrentHashMap.get(name);
                if (inputLine.contains("@")) {
                    requestCounter.increaseAll();
                    requestCounterConcurrentHashMap.replace(name, requestCounter);
                } else {
                    requestCounter.increaseAllRequestCounter();
                    requestCounterConcurrentHashMap.replace(name, requestCounter);
                }
                if (inputLine.equals(":get stat")) {
                    out.println(name + " request stat: " + requestCounter.statToString());
                } else if(inputLine.equals(":get all stat")) {
                    StringBuilder outStat = new StringBuilder();
                    requestCounterConcurrentHashMap.forEach((k, v) -> outStat.append(k));
                } else {
                    out.println(inputLine);
                }


            }
            out.close();
            in.close();
            clientSocket.close();

        }
        catch (IOException e)
        {
            if(debug) log.error ("Problem with Communication Server");
        }
    }
}