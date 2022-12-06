package client;

public class Main {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
                new GameFrame(new Tela());
        });
        t.start();
        Thread t2 = new Thread(() -> {
            new GameFrame(new Tela());
        });
        t2.start();
    }
}
