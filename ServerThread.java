import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread extends Thread {
    private Socket socket;
    String clientIP;

    public ServerThread(Socket socket) {
        super();
        this.socket = socket;
        clientIP = socket.getInetAddress().getHostAddress();
    }

    private String findUsername(String authToken){
        //find the username of the user by its id
        Iterator itr = Server.users.keySet().iterator();
        String username = null;
        while (itr.hasNext()) {
            username = (String) itr.next();
            if (Server.users.get(username).equals(authToken)){
                break;
            }
        }
        return username;
    }

    private void createAccount(ArrayList<String> input, PrintWriter out) {
        if (Server.users.containsKey(input.get(1))) {
            out.println("Sorry, the user already exists");
        } else if (!input.get(1).matches("[a-zA-Z]+")) {
            out.println("Invalid Username");
        } else {
            Account newUser = new Account(input.get(1));
            Server.users.put(input.get(1), String.valueOf(newUser.getAuthToken()));
            Server.usersData.put(input.get(1), newUser);
            out.println(newUser.getAuthToken());
        }
    }

    private void showAccounts(ArrayList<String> input, PrintWriter out) {

        //cast to int to string because I work with strings
        String authToken = input.get(1);

        //check if the authToken is valid
        if (Server.users.containsValue(authToken)) {

            String username = findUsername(authToken);

            //remove the user from the list
            HashSet accounts = new HashSet();
            accounts.addAll(Server.users.keySet());
            accounts.remove(username);
            Iterator itr = accounts.iterator();
            int counter = 1;

            //print the list with the other users
            while (itr.hasNext()) {
                out.println(counter + ". " + itr.next());
                counter++;
            }
        } else {
            out.println("Invalid Auth Token");
        }
    }

    private void sendMessage(ArrayList<String> input, PrintWriter out) {
        String authToken = input.get(1);
        if (Server.users.containsValue(authToken)) {

            String recipient = input.get(2);
            if (Server.users.containsKey(recipient)) {

                String message_body = input.get(3);

                //find the username of the user by its id
                String sender = findUsername(authToken);

                Account recipientAcc = Server.usersData.get(recipient);
                recipientAcc.addMessage(new Message(sender, recipient, message_body));

                out.println("OK");
            } else {
                out.println("User does not exist");
            }
        } else {
            out.println("Invalid Auth Token");
        }
    }

    private void showInbox(ArrayList<String> input, PrintWriter out) {
        String authToken = input.get(1);
        if (Server.users.containsValue(authToken)) {

            //find the username of the user by its id
            String username = findUsername(authToken);

            Account userAcc = Server.usersData.get(username);

            List messageBox = userAcc.getMessages();

            Iterator itr = messageBox.iterator();

            //print the list with the other users
            while (itr.hasNext()) {
                Message message = (Message) itr.next();
                if (message.getRead()) {
                    out.println(message.getId() + ". from: " + message.getSender());
                }
                else {
                    out.println(message.getId() + ". from: " + message.getSender() + "*");
                }
            }
        } else {
            out.println("Invalid Auth Token");
        }
    }

    private void readMessage(ArrayList<String> input, PrintWriter out) {
        String authToken = input.get(1);
        if (Server.users.containsValue(authToken)) {

            int message_id = Integer.parseInt(input.get(2));

            String username = findUsername(authToken);

            Account userAcc = Server.usersData.get(username);

            List messageBox = userAcc.getMessages();

            Iterator itr = messageBox.iterator();

            boolean found = false;

            while (itr.hasNext() && !found) {
                Message message = (Message) itr.next();
                if (message.getId()==message_id) {
                    found = true;
                    out.println("(" + message.getSender() + ") " + message.getBody());
                    message.read();
                }
            }
            if (!found) out.println("Message ID does not exist");

        } else {
            out.println("Invalid Auth Token");
        }
    }

    private void deleteMessage(ArrayList<String> input, PrintWriter out) {
        String authToken = input.get(1);
        if (Server.users.containsValue(authToken)) {

            int message_id = Integer.parseInt(input.get(2));

            String username = findUsername(authToken);

            Account userAcc = Server.usersData.get(username);

            List messageBox = userAcc.getMessages();

            Iterator itr = messageBox.iterator();

            boolean found = false;

            while (itr.hasNext() && !found) {
                Message message = (Message) itr.next();
                if (message.getId()==message_id) {
                    found = true;
                    messageBox.remove(message);
                    out.println("OK");
                }
            }
            if (!found) out.println("Message does not exist");

        } else {
            out.println("Invalid Auth Token");
        }
    }

    private void handleInput(ArrayList<String> input, PrintWriter out) {
        switch (input.get(0)) {
            case "1" -> createAccount(input, out);
            case "2" -> showAccounts(input, out);
            case "3" -> sendMessage(input, out);
            case "4" -> showInbox(input, out);
            case "5" -> readMessage(input, out);
            case "6" -> deleteMessage(input, out);
        }
    }


    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            ArrayList<String> input = new ArrayList<>();

            int size = Integer.parseInt(in.readLine());
            for (int i = 0; i < size; i++) {
                input.add(in.readLine());
            }

            handleInput(input, out);
            out.close();
            in.close();
            socket.close();
            System.out.println("Terminating connection. Client: " + clientIP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}