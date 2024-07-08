package util;

public class Semaphore {
    private int signals = 0;
    private int bound = 0;

    public Semaphore(int upperBound) {
        this.bound = upperBound;
    }

    public synchronized void acquire() throws InterruptedException {
        while (this.signals == bound) {
            wait();
        }
        this.signals++;
        notify();
    }

    public synchronized void release() throws InterruptedException {
        while (this.signals == 0) {
            wait();
        }
        this.signals--;
        notify();
    }
}
