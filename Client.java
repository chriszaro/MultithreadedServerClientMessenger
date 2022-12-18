import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    public static void main(String[] args) throws IOException {

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        ArrayList<String> input = new ArrayList<>();

        try {
            // the first argument is the ip address of the server
            // while the second one is its port
            socket = new Socket(args[0], Integer.parseInt(args[1]));
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            System.err.println("Could not initiate a connection to server");
            System.exit(1);
        }

        for (int i = 2; i < args.length; i++){
            input.add(args[i]);
        }

        out.println(args.length-2);
        for (int i = 0; i < args.length-2; i++){
            out.println(input.get(i));
        }

        String fromServer;
        while ((fromServer = in.readLine()) != null) {
            System.out.println(fromServer);
        }

        out.close();
        in.close();
        socket.close();
    }
}