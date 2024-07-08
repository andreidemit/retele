package util;

public class Monitor {
    private boolean busy = false;

    public synchronized void enter() throws InterruptedException {
        while (busy) {
            wait();
        }
        busy = true;
    }

    public synchronized void exit() {
        busy = false;
        notify();
    }
}
