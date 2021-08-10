package com.hit.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.String;
import java.net.Socket;

public class CacheUnitClient {

    public String send(String request) {
        String input = null;
        Socket myServer = null;
        ObjectOutputStream output = null;

        try {
            myServer = new Socket("localhost", 12345);
            output = new ObjectOutputStream(myServer.getOutputStream());
            output.writeObject(request);
            output.flush();
            input = getInputContent(new ObjectInputStream(myServer.getInputStream()));

        } catch (IOException e) {
            System.out.println("Client Error");
        }finally{
            try {
                if(myServer != null)
                    myServer.close();
                if(output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return input;
    }

    public String getInputContent(ObjectInputStream inputStream){
        try {
            String input = (String)(inputStream.readObject());
            return input;

        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Server Error");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}