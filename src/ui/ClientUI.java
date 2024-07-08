package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientUI extends JFrame {
    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientUI(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;

        setTitle("Client UI");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        // Setup main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Setup text area
        textArea = new JTextArea();
        textArea.setEditable(false);
        mainPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Setup input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        textField = new JTextField(40);
        inputPanel.add(textField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Setup settings panel
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(1, 2));

        JLabel serverLabel = new JLabel("Server Address:");
        JTextField serverField = new JTextField("localhost");
        settingsPanel.add(serverLabel);
        settingsPanel.add(serverField);

        mainPanel.add(settingsPanel, BorderLayout.NORTH);

        getContentPane().add(mainPanel);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        new Thread(new IncomingReader()).start();
    }

    private void sendMessage() {
        try {
            String message = textField.getText();
            out.writeObject(message);
            out.flush();
            textField.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String response = (String) in.readObject();
                    List<String> history = (List<String>) in.readObject();
                    
                    textArea.append("Server: " + response + "\n");
                    textArea.append("History:\n");
                    for (String entry : history) {
                        textArea.append(entry + "\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                ClientUI ui = new ClientUI(out, in);
                ui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
