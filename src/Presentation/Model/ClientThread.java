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
            boolean close = false;
            while(!close) {

                String option = ((String) this.inputStream.readUTF());

                switch (option) {

                    case "authenticate":

                        this.clientUsername = ((String) this.inputStream.readUTF());
                        String clientPassword = ((String) this.inputStream.readUTF());
                        System.out.format("Username: %s | Pwd: %s \n", clientUsername, clientPassword);
                        synchronized (this) {
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

                        do {
                            if (this.isAuthenticated) {
                                Message receivedMessage = (Message) this.inputStream.readObject();
                                System.out.format("Message: %s | From: %s | To: %s\n", receivedMessage.getText(), receivedMessage.getSenderId(), receivedMessage.getReceiverId());
                            } else {
                                break;
                            }
                        } while (((String) this.inputStream.readUTF()).equals("continue"));
                        break;

                    case "registerUser":
                        String newUserName = ((String) this.inputStream.readUTF());
                        String newPassword = ((String) this.inputStream.readUTF());
                        System.out.format("Username: %s | Pwd: %s \n", newUserName, newPassword);

                        synchronized (this) {
                            if (this.registerUser(newUserName , newPassword)) {
                                outputStream.writeUTF("Successfully Registered");
                                outputStream.flush();
                            } else {
                                outputStream.writeUTF("Not Registered");
                                outputStream.flush();
                            }

                        }
                        break;

                    case "close":
                        close = true;
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

    private boolean registerUser(String username, String password) {
        return mainController.registerUser(username, password);
    }

}
