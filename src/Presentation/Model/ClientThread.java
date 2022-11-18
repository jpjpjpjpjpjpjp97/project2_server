package Presentation.Model;

import Presentation.Controller.MainController;
import Presentation.Controller.MessageController;
import Presentation.Controller.UserController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread {
    private int clientId = 0;
    private String clientUsername = "";
    private List<User> clientList;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket clientSocket = null;
    private Server server;
    private boolean isAuthenticated;
    private MainController mainController;
    private UserController userController;
    private MessageController messageController;

    public ClientThread(Socket clientSocket, Server server, MainController mainController, UserController userController , MessageController messageController) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.mainController = mainController;
        this.userController = userController;
        this.messageController = messageController;
        this.clientList = mainController.getClientList();
    }

    public void run() {
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            boolean close = false;
            while(!close) {
                String option = ((String) this.inputStream.readUTF());
                System.out.println("Option: " + option);
                switch (option) {

                    case "authenticate":
                        this.clientUsername = ((String) this.inputStream.readUTF());
                        String clientPassword = ((String) this.inputStream.readUTF());
                        System.out.format("Username: %s | Pwd: %s \n", clientUsername, clientPassword);
                        synchronized (this) {
                            outputStream.writeInt(this.authenticate(clientUsername, clientPassword));
                            outputStream.flush();
                        }

                        break;

                    case "listUsers":
                        ArrayList<User> userList = (ArrayList<User>) this.listUsers();
                        outputStream.writeObject(userList);
                        break;

                    case "filterUsers":
                        String filterText = ((String) this.inputStream.readUTF());
                        ArrayList<User> filteredUserList = (ArrayList<User>) this.filterUsers(filterText);
                        outputStream.writeObject(filteredUserList);
                        break;

                    case "getIdForUsername":
                        String contactUsername = ((String) this.inputStream.readUTF());
                        int contactUserId = this.getUserId(contactUsername);
                        outputStream.writeInt(contactUserId);
                        outputStream.flush();
                        break;

                    case "registerUser":
                        String newUserName = ((String) this.inputStream.readUTF());
                        String newPassword = ((String) this.inputStream.readUTF());
                        System.out.format("Username: %s | Pwd: %s \n", newUserName, newPassword);

                        synchronized (this) {
                            outputStream.writeInt(this.registerUser(newUserName , newPassword));
                            outputStream.flush();
                        }
                        break;

                    case "updateUser":
                        String upId = ((String) this.inputStream.readUTF());
                        String upUserName = ((String) this.inputStream.readUTF());
                        String upPassword = ((String) this.inputStream.readUTF());
                        System.out.format("Update username: %s | update Pwd: %s \n", upUserName, upPassword);

                        synchronized (this) {
                            if (this.updateUser(Integer.parseInt(upId) , upUserName , upPassword)) {
                                outputStream.writeUTF("Successfully Updated");
                                outputStream.flush();
                            } else {
                                outputStream.writeUTF("Not Updated");
                                outputStream.flush();
                            }
                        }
                        break;

                    case "pendingMessages":
                        int userId = this.inputStream.readInt();
                        System.out.format("Messages recipient: %s \n", userId);
                        ArrayList<Message> pendingMessages = (ArrayList<Message>) this.getPendingMessages(userId);
                        outputStream.writeObject(pendingMessages);
                        break;

                    case "listMessages":
                        ArrayList<Message> messageList = (ArrayList<Message>) this.listMessages();
                        outputStream.writeObject(messageList);
                        break;

                    case "filterMessages":
                        String filterMessage = ((String) this.inputStream.readUTF());
                        ArrayList<Message> filteredMessageList = (ArrayList<Message>) this.filterMessages(filterMessage);
                        outputStream.writeObject(filteredMessageList );
                        break;

                    case "getNewMessages":
                        int receiverId = this.inputStream.readInt();
                        ArrayList<Message> newMessageList = (ArrayList<Message>) this.getNewMessages(receiverId);
                        outputStream.writeObject(newMessageList);
                        break;

                    case "addMessage":
                        Message newMessage = ((Message) this.inputStream.readObject());
                        System.out.format("Message: %s | Sender: %s | Receiver: %s \n", newMessage.toString(), newMessage.getSenderId(), newMessage.getReceiverId());

                        synchronized (this) {
                            if (this.addMessage(newMessage)) {
                                outputStream.writeUTF("Successfully added");
                                outputStream.flush();
                            } else {
                                outputStream.writeUTF("Not added");
                                outputStream.flush();
                            }
                        }
                        break;

                    case "PendingMessages":
                        int idReceived = this.inputStream.readInt();
                        ArrayList<Message> newMessageList2 = (ArrayList<Message>) this.getPendingMessages(idReceived);
                        outputStream.writeObject(newMessageList2);
                        break;


                    case "updateMessage":
                        String upIdMessage = ((String) this.inputStream.readUTF());
                        String upText = ((String) this.inputStream.readUTF());
                        String id_conversation = ((String) this.inputStream.readUTF());
                        System.out.format("Update Text: %s | update Id_conversation: %s \n", upText, id_conversation);

                        synchronized (this) {
                            if (this.updateMessage(Integer.parseInt(upIdMessage) , upText , Integer.parseInt(id_conversation))) {
                                outputStream.writeUTF("Successfully Updated");
                                outputStream.flush();
                            } else {
                                outputStream.writeUTF("Not Updated");
                                outputStream.flush();
                            }
                        }
                        break;

                    case "deleteMessage":
                        int deleteIdMessage = this.inputStream.readInt();
                        System.out.format("Delete id: %s \n", deleteIdMessage);

                        synchronized (this) {
                            if (this.deleteMessage(deleteIdMessage)) {
                                outputStream.writeUTF("Successfully Deleted");
                                outputStream.flush();
                            } else {
                                outputStream.writeUTF("Not Deleted");
                                outputStream.flush();
                            }
                        }
                        break;

                    case "isOnline":
                        int id = this.inputStream.readInt();
                        String username = this.inputStream.readUTF();
                        synchronized (this) {
                            if (server.isOnline(id, username)) {
                                outputStream.writeInt(1);
                                outputStream.flush();
                            } else {
                                outputStream.writeInt(0);
                                outputStream.flush();
                            }
                        }
                        break;

                    case "close":
                        close = true;
                        break;
                }
                if (!close) System.out.println("Waiting for next input from client...");
            }
            System.out.println("Closing connection with " + this.clientUsername);
            this.inputStream.close();
            this.outputStream.close();
            this.server.closeClient(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private int authenticate(String clientName, String clientPassword) {
        return userController.authenticate(clientUsername, clientPassword);
    }

    private List<User> listUsers() {
        return userController.listUsers();
    }

    private List<Message> listMessages() {
        return messageController.listMessages();
    }

    private List<Message> filterMessages(String text) {
        return messageController.filterMessages(text);
    }

    private List<Message> getNewMessages(int receiverId) {
        return messageController.getNewMessages(receiverId);
    }

    private boolean addMessage(Message message) {
        return messageController.addMessage(message);
    }

    private boolean updateMessage(int id, String text, int  id_conversation) {
        return messageController.updateMessage(id,text, id_conversation);
    }

    private boolean deleteMessage(int id) {
        return messageController.deleteMessage(id);
    }



    private List<User> filterUsers(String text) {
        return userController.filterUsers(text);
    }

    private int getUserId(String contactUsername) {
        return userController.getUserId(contactUsername);
    }



    private int registerUser(String username, String password) {
        return userController.registerUser(username, password);
    }

    private boolean updateUser(int id, String username, String password) {
        return userController.updateUser(id,username, password);
    }

    private boolean deleteUser(int id) {
        return userController.deleteUser(id);
    }

    private List<Message> getPendingMessages(int id) {
        return messageController.getPendingMessages(id);
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public List<User> getClientList() {
        return clientList;
    }

    public void setClientList(List<User> clientList) {
        this.clientList = clientList;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public UserController getUserController() {
        return userController;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public MessageController getMessageController() {
        return messageController;
    }

    public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
    }

    public boolean isOnline() {
        return true;
    }
}
