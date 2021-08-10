package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Runnable;
import java.lang.reflect.Type;

public class HandleRequest<T> implements Runnable {

    Socket socket;
    CacheUnitController<T> controller;

    // Constructor
    public HandleRequest(Socket s, CacheUnitController<T> controller) {
        socket = s;
        this.controller = controller;
    }

    @Override
    public void run() {

        ObjectOutputStream out;
        ObjectInputStream in;
        Request<DataModel<T>[]> request;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            String req = (String)in.readObject();
            if (req.equals("showStatistics")) {
                out.writeObject(controller.getStatistics());
            } else { // A get/update/delete request
                Type ref = new TypeToken<Request<DataModel<T>[]>>(){}.getType();
                request = new Gson().fromJson(req, ref);
                String actionType = request.getHeaders().get("action");

                if (actionType != null) {

                    switch (actionType) {
                        case "GET": {
                            DataModel[] response = controller.get(request.getBody());
                            if (response != null) {
                                out.writeObject(response.toString());
                            }
                            break;
                        }
                        case "UPDATE": {
                            Boolean response = controller.update(request.getBody());
                            out.writeObject(response.toString());
                            break;
                        }
                        case "DELETE": {
                            Boolean response = controller.delete(request.getBody());
                            out.writeObject(response.toString());
                            break;
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}