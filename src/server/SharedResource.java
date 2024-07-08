package server;

import java.util.ArrayList;
import java.util.List;

public class SharedResource {
    private List<String> history;

    public SharedResource() {
        history = new ArrayList<>();
    }

    public synchronized String processRequest(String request) {
        // Procesarea cererii și adăugarea acesteia în istoric
        String response = "Processed: " + request;
        history.add(response);
        return response;
    }

    public synchronized List<String> getHistory() {
        return new ArrayList<>(history);
    }
}
