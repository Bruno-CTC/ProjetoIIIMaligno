package SingleplayerPK;

public class Main {

    public static void main(String[] args){
        try {
            new GameFrame();
        } catch (Exception e) {
            System.err.println("Erro ao inicar tela do jogo: " + e.getMessage());
        }
    }
}
