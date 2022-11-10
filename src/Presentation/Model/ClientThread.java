package Presentation.Model;

import Presentation.Controller.MainController;
import Presentation.Model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Thread {
    private String clientUsername = null;
    private List<User> clientList;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private Socket clientSocket = null;
    private boolean isAuthenticated;
    private MainController mainController;

    public ClientThread(Socket clientSocket, MainController mainController) {
        this.clientSocket = clientSocket;
        this.mainController = mainController;
        this.clientList = mainController.getClientList();
    }

    public void run() {
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.clientUsername = ((String) this.inputStream.readUTF());
            String clientPassword = ((String) this.inputStream.readUTF());
            System.out.format("Username: %s | Pwd: %s \n", clientUsername, clientPassword);
            synchronized(this) {
                if (this.authenticate(clientUsername, clientPassword)) {
                    this.isAuthenticated = true;
                    outputStream.writeUTF("Successfully Authenticated");
                    outputStream.flush();
                } else {
                    outputStream.writeUTF("Not Authenticated");
                    outputStream.flush();
                    System.out.println("Username or password are not correct.");
                }

                if (this.isAuthenticated) {
                    mainController.openChatWindow();
                } else {
                    mainController.setError("Username or password are not correct. Unable to authenticate.");
                }
            }

            while (true) {
                if (this.isAuthenticated) {
                    Message receivedMessage = (Message) this.inputStream.readObject();
                    System.out.format("Message: %s | From: %s | To: %s", receivedMessage.getText(), receivedMessage.getSenderId(), receivedMessage.getReceiverId());
                }
                else {
                    break;
                }
            }

            System.out.println("Closing connection with " + this.clientUsername);
            this.inputStream.close();
            this.outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean authenticate(String clientName, String clientPassword) {
        return mainController.authenticate(clientUsername, clientPassword);
    }
}
