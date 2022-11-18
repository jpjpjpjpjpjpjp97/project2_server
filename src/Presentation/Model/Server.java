package Presentation.Model;

import Presentation.Controller.MainController;
import Presentation.Controller.MessageController;
import Presentation.Controller.UserController;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.util.Objects;

public class Server {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static UserController userController;

    private static MessageController messageController;
    private int portNumber;
    private static ArrayList<ClientThread> clientList;
    private int numberOfClients;
    private MainController mainController;

    public Server(MainController mainController, UserController userController , MessageController messageController) {
        this.mainController = mainController;
        this.userController = userController;
        this.messageController = messageController;
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
                ClientThread newClient =  new ClientThread(clientSocket, this, mainController, userController , messageController);
                newClient.start();
                clientList.add(newClient);
                System.out.println("Client connected!");
                this.numberOfClients++;
                System.out.println(numberOfClients + " clients connected at the moment!");

            } catch (IOException e) {
                System.out.println("Client could not be connected");
            }
        }
    }

    /*public void sendMessage(Message message, int receiverId){

    }*/

    public boolean isOnline(int receiverId , String username){
        for (ClientThread client : clientList) {
            if (Objects.equals(client.getClientUsername(), username)){
                return true;
            }
        }
        return false;
    }

    public void closeClient(ClientThread closingClient){
        this.clientList.remove(closingClient);
        this.numberOfClients --;
    }
}
