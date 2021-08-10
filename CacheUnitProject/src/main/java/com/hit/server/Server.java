package com.hit.server;

import com.hit.services.CacheUnitController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements PropertyChangeListener, Runnable {

    private ServerSocket socket;
    private Boolean runServer;
    private final String LRU = "LRU";
    private final String MFU = "MFU";
    private final String RANDOM = "RANDOM";
    public static Integer numberOfRequests;
    private String algoType;
    private Integer capacity;

    // Constructor
    public Server(){
        runServer = false;
        algoType = null;
        capacity = 0;
        numberOfRequests = 0;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getNewValue().equals("START")){
            setCapacity();
            setAlgoType();
            this.runServer = true;
            Thread thread = new Thread(this);
            thread.start();
        }
        else{ // Close the connection
            try {
                this.runServer = false;
                this.socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
        try {
            socket = new ServerSocket(12345);
            ExecutorService executor = Executors.newCachedThreadPool();
            while(this.runServer){
                try {
                    Socket request = this.socket.accept();
                    numberOfRequests++;
                    executor.execute(new HandleRequest(request, new CacheUnitController<String>(algoType, capacity)) );
                } catch (IOException ignored) {
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Get the capacity from the user.
    private void setCapacity() {
        Scanner scanner = new Scanner(System.in);
        boolean valid = false;
        Integer capacityInput = null;

        while (!valid) {
            System.out.println("Please enter your cache capacity: ");

            try {
                capacityInput = scanner.nextInt();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, Try again please.");
            }
        }

        capacity = capacityInput;
    }

    // Get the algoType from the user.
    private void setAlgoType() {
        Scanner scanner = new Scanner(System.in);
        String algoTypeInput = null;

        System.out.println("Please enter your desired algorithm (LRU, MFU, RANDOM): ");
        algoTypeInput = scanner.nextLine().toUpperCase();

        while((!(algoTypeInput.equals(LRU)) && !(algoTypeInput.equals(MFU)) && !(algoTypeInput.equals(RANDOM)))) {
            System.out.println("Invalid input, Please enter your desired algorithm: ");
            algoTypeInput = scanner.nextLine().toUpperCase();;
        }

        algoType = algoTypeInput;
    }
}


