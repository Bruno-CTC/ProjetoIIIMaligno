package SingleplayerPK;

public class Movimento {

    int x, y;
    Peca pecaAMover;

    public Movimento(int x, int y, Peca pecaAMover) {
        this.x = x;
        this.y = y;
        this.pecaAMover = pecaAMover;
    }

    // Getters:

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Peca getPecaAMover() {
        return pecaAMover;
    }
}
