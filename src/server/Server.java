package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import util.Semaphore;
import util.Monitor;

public class Server {
    private static final int PORT = 12345;
    private static SharedResource resource = new SharedResource();
    private static Semaphore semaphore = new Semaphore(1);
    private static Monitor monitor = new Monitor();
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("Server is starting...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());
                executorService.submit(new ClientHandler(socket));
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                
                String message;
                while ((message = (String) in.readObject()) != null) {
                    System.out.println("Received from client (" + socket.getInetAddress().getHostAddress() + "): " + message);
                    
                    // Using semaphore to ensure exclusive access to the shared resource
                    semaphore.acquire();
                    System.out.println("Semaphore acquired by client (" + socket.getInetAddress().getHostAddress() + ")");
                    String response = resource.processRequest(message);
                    semaphore.release();
                    System.out.println("Semaphore released by client (" + socket.getInetAddress().getHostAddress() + ")");

                    out.writeObject(response);
                    out.writeObject(resource.getHistory());
                    out.flush();
                    System.out.println("Response and history sent to client (" + socket.getInetAddress().getHostAddress() + ")");
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                System.err.println("Error handling client (" + socket.getInetAddress().getHostAddress() + "): " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    System.out.println("Connection closed with client: " + socket.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    System.err.println("Error closing connection with client (" + socket.getInetAddress().getHostAddress() + "): " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
