package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import ui.ClientUI;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            ClientUI ui = new ClientUI(out, in);
            ui.setVisible(true);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                out.writeObject(message);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
