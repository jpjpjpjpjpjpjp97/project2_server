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
    private String clientUsername = null;
    private List<User> clientList;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private Socket clientSocket = null;
    private boolean isAuthenticated;
    private MainController mainController;
    private UserController userController;
    private MessageController messageController;

    public ClientThread(Socket clientSocket, MainController mainController, UserController userController , MessageController messageController) {
        this.clientSocket = clientSocket;
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

                    case "listUsers":
                        ArrayList<User> userList = (ArrayList<User>) this.listUsers();
                        outputStream.writeObject(userList);
                        break;

                    case "filterUsers":
                        String filterText = ((String) this.inputStream.readUTF());
                        ArrayList<User> filteredUserList = (ArrayList<User>) this.filterUsers(filterText);
                        outputStream.writeObject(filteredUserList);
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

                    case "deleteUser":
                        String deleteId = ((String) this.inputStream.readUTF());
                        System.out.format("Delete id: %s \n", deleteId);

                        synchronized (this) {
                            if (this.deleteUser(Integer.parseInt(deleteId))) {
                                outputStream.writeUTF("Successfully Deleted");
                                outputStream.flush();
                            } else {
                                outputStream.writeUTF("Not Deleted");
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

                    case "addMessage":
                        String newText = ((String) this.inputStream.readUTF());
                        String newId_conversation = ((String) this.inputStream.readUTF());
                        System.out.format("Text: %s | id_conversation: %s \n", newText, newId_conversation);

                        synchronized (this) {
                            if (this.addMessage(newText , Integer.parseInt(newId_conversation))) {
                                outputStream.writeUTF("Successfully added");
                                outputStream.flush();
                            } else {
                                outputStream.writeUTF("Not added");
                                outputStream.flush();
                            }

                        }
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
                        String deleteId_Message = ((String) this.inputStream.readUTF());
                        System.out.format("Delete id: %s \n", deleteId_Message);

                        synchronized (this) {
                            if (this.deleteMessage(Integer.parseInt(deleteId_Message ))) {
                                outputStream.writeUTF("Successfully Deleted");
                                outputStream.flush();
                            } else {
                                outputStream.writeUTF("Not Deleted");
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

    private boolean addMessage(String text, int id_conversation) {
        return messageController.addMessage(text, id_conversation);
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


    private boolean registerUser(String username, String password) {
        return userController.registerUser(username, password);
    }

    private boolean updateUser(int id, String username, String password) {
        return userController.updateUser(id,username, password);
    }

    private boolean deleteUser(int id) {
        return userController.deleteUser(id);
    }

    private Object getPendingMessages(int id) {
        return userController.deleteUser(id);
    }
}
