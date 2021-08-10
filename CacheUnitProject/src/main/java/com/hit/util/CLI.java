package com.hit.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CLI implements Runnable {

    private String command = new String("");
    private PropertyChangeSupport support;
    private Scanner scanner;
    private PrintWriter printWriter;

    public CLI(InputStream in, OutputStream out){
        scanner = new Scanner(in);
        printWriter = new PrintWriter(out);
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void run() {
        this.write("Enter a command (START/SHUTDOWN/EXIT)");
        while(!command.equals("EXIT")){
            String prevCommand = new String(this.command);
            this.command = scanner.next();
            if(this.command.equals("START")){
                this.write("Starting server...");
                this.support.firePropertyChange("command", prevCommand, this.command);
            }
            else if(this.command.equals("SHUTDOWN")){
                this.write("Shutdown server...");
                this.support.firePropertyChange("command", prevCommand, this.command);
            }
            else if(!this.command.equals("EXIT")){
                this.write("Illegal command, Please enter 'START', 'SHUTDOWN', or 'EXIT'");
            }
        }
        return;
    }


    public void addPropertyChangeListener(PropertyChangeListener pcl){
        this.support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl){
        this.support.removePropertyChangeListener(pcl);
    }

    public void write(String string){

        System.out.println(string);
    }

}

