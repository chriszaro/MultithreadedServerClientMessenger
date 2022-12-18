import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    public static HashMap<String, String> users = new HashMap<String, String>();
    public static HashMap<String, Account> usersData = new HashMap<String, Account>();

    public static void main(String[] args) throws IOException {
        ServerSocket socket = null;
        // the first argument is the port to listen on
        String port = args[0];

//        users.put("mac", "1");
//        usersData.put("mac", new Account("mac"));
//
//        users.put("john", "2");
//        usersData.put("john", new Account("john"));


        try {
            socket = new ServerSocket(Integer.parseInt(port));
            System.out.println("Server is now running @ " + args[0]);
            while (true) {
                Socket clientConnection = socket.accept();
                ServerThread st = new ServerThread(clientConnection);
                st.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!socket.isClosed()) socket.close();
        }
    }
}

class Message{
    private boolean isRead;
    private String sender;
    private String receiver;
    private String body;

    private int id;

    public Message(String sender, String receiver, String body){
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
        isRead = false;
        id = Server.usersData.get(receiver).numberOfMsg() + 1;
    }

    public boolean getRead(){
        return isRead;
    }

    public void read(){
        isRead = true;
    }

    public String getSender(){
        return sender;
    }

    public String getBody(){
        return body;
    }

    public int getId(){
        return id;
    }
}

class Account{
    private String username;
    private int authToken;
    private List<Message> messageBox;

    private int totalMessages;

    public Account(String username){
        this.username = username;
        authToken = Server.users.size() + 1;
        messageBox = new ArrayList<>();
        totalMessages = 0;
    }

    public int getAuthToken(){
        return authToken;
    }

    public void addMessage(Message newMsg){
        totalMessages++;
        messageBox.add(newMsg);
    }

    public int numberOfMsg(){
        return totalMessages;
    }

    public List getMessages(){
        return messageBox;
    }
}
