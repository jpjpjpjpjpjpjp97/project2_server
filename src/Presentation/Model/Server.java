package Presentation.Model;

import Presentation.Controller.MainController;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;

public class Server {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private int portNumber;
    private static ArrayList<ClientThread> clientList;
    private int numberOfClients;
    private MainController mainController;

    public Server(MainController mainController) {
        this.mainController = mainController;
        this.portNumber = 8080;
        this.numberOfClients = 0;
        this.clientList = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println("Server Socket cannot be created");
        }
    }

    public void start(){
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                ClientThread newClient =  new ClientThread(clientSocket, mainController);
                newClient.start();
                clientList.add(newClient);
                System.out.println("Client connected!");
                this.numberOfClients++;

            } catch (IOException e) {
                System.out.println("Client could not be connected");
            }
        }
    }
}
