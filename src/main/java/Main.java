import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {

        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
//        String word = "Бизнес";
//        System.out.println(engine.search(word.toLowerCase()));
//
        int port = 8989;

        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(port);
                 Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                Gson gson = new Gson();
                String word = in.readLine();
                out.println(gson.toJson(engine.search(word.toLowerCase())));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}