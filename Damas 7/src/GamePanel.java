import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;

public class GamePanel extends JPanel implements ActionListener {

    static Celula[][] tabuleiro = new Celula[8][8];
    static Peca pecaSelecionada;
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = SCREEN_HEIGHT / 8;

    static Peca.Cor cliente = Peca.Cor.PRETO;
    static boolean puloMulti = false;
    static boolean[][] possiveisPulos = new boolean[8][8];

    static Peca.Cor vez = Peca.Cor.BRANCO;

    static int numBrancas = 12;
    static int numPretas = 12;


    public GamePanel() throws IOException {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addMouseListener(new MyMouseAdapter());
        this.addMouseMotionListener(new MyMouseAdapter());

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tabuleiro[i][j] = new Celula(j, i);
            }
        }

        startGame();
    }

    private void startGame() throws IOException {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                tabuleiro[i][j * 2 + (i % 2)].peca = new Peca(j * 2 + (i % 2), i, Peca.Cor.BRANCO);
            }
        }

        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                tabuleiro[i][j * 2 + (i % 2)].peca = new Peca(j * 2 + (i % 2), i, Peca.Cor.PRETO);
            }
        }
    }

    public void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) throws IOException {

        // Desenha o tabuleiro:
        g.setColor(new Color(226, 169, 120));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g.setColor(new Color(82, 48, 34));

        for (int i = 0; i < UNIT_SIZE; i++) {
            for (int j = 0; j < UNIT_SIZE; j += 2) {
                g.fillRect((j + (i % 2)) * UNIT_SIZE, i * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }

        // Desenha as peças:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j].peca != null) {
                    tabuleiro[i][j].peca.draw(g);
                }
            }
        }

        if (pecaSelecionada != null) {
            pecaSelecionada.drawCirc(g);
        }

        // Desenha possíveis pulos:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (possiveisPulos[i][j]) {
                    g.setColor(new Color(255, 228, 228, 100));
                    g.fillOval(j * UNIT_SIZE + UNIT_SIZE / 4 + 5 / 2, i * UNIT_SIZE + UNIT_SIZE / 4 + 5 / 2, UNIT_SIZE / 2 - 5, UNIT_SIZE / 2 - 5);
                }
            }
        }
    }

    // Verifica os possíveis pulos:
    public void checkJumps(int linha, int coluna) {
        if (linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8) {

            puloMulti = false;

            if (linha - 2 >= 0 && coluna - 2 >= 0
                    && tabuleiro[linha - 1][coluna - 1].peca != null && tabuleiro[linha - 1][coluna - 1].peca.cor != pecaSelecionada.cor) {
                if (tabuleiro[linha - 2][coluna - 2].peca == null) {
                    possiveisPulos[linha - 2][coluna - 2] = true;
                    puloMulti = true;
                }
            }
            if (linha - 2 >= 0 && coluna + 2 < 8
                    && tabuleiro[linha - 1][coluna + 1].peca != null && tabuleiro[linha - 1][coluna + 1].peca.cor != pecaSelecionada.cor) {
                if (tabuleiro[linha - 2][coluna + 2].peca == null) {
                    possiveisPulos[linha - 2][coluna + 2] = true;
                    puloMulti = true;
                }
            }
            if (linha + 2 < 8 && coluna - 2 >= 0
                    && tabuleiro[linha + 1][coluna - 1].peca != null && tabuleiro[linha + 1][coluna - 1].peca.cor != pecaSelecionada.cor) {
                if (tabuleiro[linha + 2][coluna - 2].peca == null) {
                    possiveisPulos[linha + 2][coluna - 2] = true;
                    puloMulti = true;
                }
            }
            if (linha + 2 < 8 && coluna + 2 < 8
                    && tabuleiro[linha + 1][coluna + 1].peca != null && tabuleiro[linha + 1][coluna + 1].peca.cor != pecaSelecionada.cor) {
                if (tabuleiro[linha + 2][coluna + 2].peca == null) {
                    possiveisPulos[linha + 2][coluna + 2] = true;
                    puloMulti = true;
                }
            }
        }
    }

    // Verifica os possíveis pulos:
    public void checkJumps() {
        int linha = pecaSelecionada.getX();
        int coluna = pecaSelecionada.getY();

        if (linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8) {

            puloMulti = false;

            if (linha - 2 >= 0 && coluna - 2 >= 0
                    && tabuleiro[linha - 1][coluna - 1].peca != null && tabuleiro[linha - 1][coluna - 1].peca.cor != pecaSelecionada.cor) {
                if (tabuleiro[linha - 2][coluna - 2].peca == null) {
                    possiveisPulos[linha - 2][coluna - 2] = true;
                    puloMulti = true;
                }
            }
            if (linha - 2 >= 0 && coluna + 2 < 8
                    && tabuleiro[linha - 1][coluna + 1].peca != null && tabuleiro[linha - 1][coluna + 1].peca.cor != pecaSelecionada.cor) {
                if (tabuleiro[linha - 2][coluna + 2].peca == null) {
                    possiveisPulos[linha - 2][coluna + 2] = true;
                    puloMulti = true;
                }
            }
            if (linha + 2 < 8 && coluna - 2 >= 0
                    && tabuleiro[linha + 1][coluna - 1].peca != null && tabuleiro[linha + 1][coluna - 1].peca.cor != pecaSelecionada.cor) {
                if (tabuleiro[linha + 2][coluna - 2].peca == null) {
                    possiveisPulos[linha + 2][coluna - 2] = true;
                    puloMulti = true;
                }
            }
            if (linha + 2 < 8 && coluna + 2 < 8
                    && tabuleiro[linha + 1][coluna + 1].peca != null && tabuleiro[linha + 1][coluna + 1].peca.cor != pecaSelecionada.cor) {
                if (tabuleiro[linha + 2][coluna + 2].peca == null
                        && tabuleiro[linha + 2][coluna + 2].peca != pecaSelecionada) {
                    possiveisPulos[linha + 2][coluna + 2] = true;
                    puloMulti = true;
                }

            }
        }
    }

    // Verifica os pulos da dama:
    public void checkJumpsDama(int linha, int coluna) {
        if (linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8) {

            puloMulti = false;

            // Verifica para cima e para a esquerda:
            for (int i = 1; i < 8; i++) {
                if (linha - i >= 0 && coluna - i >= 0) {
                    if (tabuleiro[linha - i][coluna - i].peca == null) {
                        possiveisPulos[linha - i][coluna - i] = true;
                        puloMulti = true;
                    } else {
                        if (tabuleiro[linha - i][coluna - i].peca.cor != pecaSelecionada.cor) {
                            if (linha - i - 1 >= 0 && coluna - i - 1 >= 0) {
                                if (tabuleiro[linha - i - 1][coluna - i - 1].peca == null) {
                                    possiveisPulos[linha - i - 1][coluna - i - 1] = true;
                                    puloMulti = true;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            // Verifica para cima e para a direita:
            for (int i = 1; i < 8; i++) {
                if (linha - i >= 0 && coluna + i < 8) {
                    if (tabuleiro[linha - i][coluna + i].peca == null) {
                        possiveisPulos[linha - i][coluna + i] = true;
                        puloMulti = true;
                    } else {
                        if (tabuleiro[linha - i][coluna + i].peca.cor != pecaSelecionada.cor) {
                            if (linha - i - 1 >= 0 && coluna + i + 1 < 8) {
                                if (tabuleiro[linha - i - 1][coluna + i + 1].peca == null) {
                                    possiveisPulos[linha - i - 1][coluna + i + 1] = true;
                                    puloMulti = true;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            // Verifica para baixo e para a esquerda:
            for (int i = 1; i < 8; i++) {
                if (linha + i < 8 && coluna - i >= 0) {
                    if (tabuleiro[linha + i][coluna - i].peca == null) {
                        possiveisPulos[linha + i][coluna - i] = true;
                        puloMulti = true;
                    } else {
                        if (tabuleiro[linha + i][coluna - i].peca.cor != pecaSelecionada.cor) {
                            if (linha + i + 1 < 8 && coluna - i - 1 >= 0) {
                                if (tabuleiro[linha + i + 1][coluna - i - 1].peca == null) {
                                    possiveisPulos[linha + i + 1][coluna - i - 1] = true;
                                    puloMulti = true;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            // Verifica para baixo e para a direita:
            for (int i = 1; i < 8; i++) {
                if (linha + i < 8 && coluna + i < 8) {
                    if (tabuleiro[linha + i][coluna + i].peca == null) {
                        possiveisPulos[linha + i][coluna + i] = true;
                        puloMulti = true;
                    } else {
                        if (tabuleiro[linha + i][coluna + i].peca.cor != pecaSelecionada.cor) {
                            if (linha + i + 1 < 8 && coluna + i + 1 < 8) {
                                if (tabuleiro[linha + i + 1][coluna + i + 1].peca == null) {
                                    possiveisPulos[linha + i + 1][coluna + i + 1] = true;
                                    puloMulti = true;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    // Verifica os pulos da dama:
    public void checkJumpsDama() {
        puloMulti = false;
        int linha = pecaSelecionada.getX();
        int coluna = pecaSelecionada.getY();
        // Verifica para cima e para a esquerda:
        for (int i = 1; i < 8; i++) {
            if (linha - i >= 0 && coluna - i >= 0) {
                if (tabuleiro[linha - i][coluna - i].peca == null) {
                    possiveisPulos[linha - i][coluna - i] = true;
                    puloMulti = true;
                } else {
                    if (tabuleiro[linha - i][coluna - i].peca.cor != pecaSelecionada.cor) {
                        if (linha - i - 1 >= 0 && coluna - i - 1 >= 0) {
                            if (tabuleiro[linha - i - 1][coluna - i - 1].peca == null) {
                                possiveisPulos[linha - i - 1][coluna - i - 1] = true;
                                puloMulti = true;
                            }
                        }
                    }
                    break;
                }
            }
        }

        // Verifica para cima e para a direita:
        for (int i = 1; i < 8; i++) {
            if (linha - i >= 0 && coluna + i < 8) {
                if (tabuleiro[linha - i][coluna + i].peca == null) {
                    possiveisPulos[linha - i][coluna + i] = true;
                    puloMulti = true;
                } else {
                    if (tabuleiro[linha - i][coluna + i].peca.cor != pecaSelecionada.cor) {
                        if (linha - i - 1 >= 0 && coluna + i + 1 < 8) {
                            if (tabuleiro[linha - i - 1][coluna + i + 1].peca == null) {
                                possiveisPulos[linha - i - 1][coluna + i + 1] = true;
                                puloMulti = true;
                            }
                        }
                    }
                    break;
                }
            }
        }

        // Verifica para baixo e para a esquerda:
        for (int i = 1; i < 8; i++) {
            if (linha + i < 8 && coluna - i >= 0) {
                if (tabuleiro[linha + i][coluna - i].peca == null) {
                    possiveisPulos[linha + i][coluna - i] = true;
                    puloMulti = true;
                } else {
                    if (tabuleiro[linha + i][coluna - i].peca.cor != pecaSelecionada.cor) {
                        if (linha + i + 1 < 8 && coluna - i - 1 >= 0) {
                            if (tabuleiro[linha + i + 1][coluna - i - 1].peca == null) {
                                possiveisPulos[linha + i + 1][coluna - i - 1] = true;
                                puloMulti = true;
                            }
                        }
                    }
                    break;
                }
            }
        }

        // Verifica para baixo e para a direita:
        for (int i = 1; i < 8; i++) {
            if (linha + i < 8 && coluna + i < 8) {
                if (tabuleiro[linha + i][coluna + i].peca == null) {
                    possiveisPulos[linha + i][coluna + i] = true;
                    puloMulti = true;
                } else {
                    if (tabuleiro[linha + i][coluna + i].peca.cor != pecaSelecionada.cor) {
                        if (linha + i + 1 < 8 && coluna + i + 1 < 8) {
                            if (tabuleiro[linha + i + 1][coluna + i + 1].peca == null) {
                                possiveisPulos[linha + i + 1][coluna + i + 1] = true;
                                puloMulti = true;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    // Movimenta a peça selecionada:
    public void movePeca(int linha, int coluna, boolean pulo) {
        if (linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8) {
            if (tabuleiro[linha][coluna].peca == null) {
                tabuleiro[pecaSelecionada.getY()][pecaSelecionada.getX()].peca = null;
                tabuleiro[linha][coluna].peca = pecaSelecionada;
                pecaSelecionada.setY(linha);
                pecaSelecionada.setX(coluna);
                possiveisPulos = new boolean[8][8];

                checkDama();

                if (pulo) {
                    checkJumps();
                }
                if (!puloMulti) {
                    pecaSelecionada = null;
                    vez = vez == Peca.Cor.BRANCO ? Peca.Cor.PRETO : Peca.Cor.BRANCO;
                }
            }
        }
    }

    // Verifica se o movimento é válido a partir da cor da peça:
    public boolean checkMove(int linha, int coluna) {
        if (linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8) {
            if (pecaSelecionada.cor == Peca.Cor.BRANCO) {
                if (linha - 1 >= 0 && coluna - 1 >= 0) {
                    if (linha - 1 == pecaSelecionada.getY() && coluna - 1 == pecaSelecionada.getX()) {
                        return true;
                    }
                }
                if (linha - 1 >= 0 && coluna + 1 < 8) {
                    return linha - 1 == pecaSelecionada.getY() && coluna + 1 == pecaSelecionada.getX();
                }
            } else {
                if (linha + 1 < 8 && coluna - 1 >= 0) {
                    if (linha + 1 == pecaSelecionada.getY() && coluna - 1 == pecaSelecionada.getX()) {
                        return true;
                    }
                }
                if (linha + 1 < 8 && coluna + 1 < 8) {
                    return linha + 1 == pecaSelecionada.getY() && coluna + 1 == pecaSelecionada.getX();
                }
            }
        }

        return false;
    }

    // Come peça:
    public void eatPeca(int linha, int coluna) {
        if (linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8) {
            tabuleiro[linha][coluna].peca = null;

            if (vez == Peca.Cor.BRANCO) {
                numBrancas--;
            } else {
                numPretas--;
            }
            if (numBrancas == 0 || numPretas == 0) {
                JOptionPane.showMessageDialog(null, "Fim de jogo!", "Fim de jogo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Verifica se a peça virou uma dama:
    public void checkDama() {
        if (pecaSelecionada.cor == Peca.Cor.BRANCO && pecaSelecionada.x == 0) {
            pecaSelecionada.isDama = true;
            System.out.println("Dama branca");
        } else if (pecaSelecionada.cor == Peca.Cor.PRETO && pecaSelecionada.x == 7) {
            pecaSelecionada.isDama = true;
            System.out.println("Dama preta");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int linha = e.getY() / UNIT_SIZE;
            int coluna = e.getX() / UNIT_SIZE;

            Celula celulaSelecionada = tabuleiro[linha][coluna];

            if (celulaSelecionada.peca == pecaSelecionada) {
                pecaSelecionada = null;
                possiveisPulos = new boolean[8][8];
            } else if (celulaSelecionada.peca != null && celulaSelecionada.peca.cor == vez) {
                pecaSelecionada = celulaSelecionada.peca;
                possiveisPulos = new boolean[8][8];
                if (pecaSelecionada.isDama) {
                    checkJumpsDama();
                }
                checkJumps();
            }

            if (pecaSelecionada != null && (celulaSelecionada.peca == null)) {
                if (!haPulos()) {
                    if (checkMove(linha, coluna)) {
                        puloMulti = false;
                        movePeca(linha, coluna, false);
                    }
                } else if (possiveisPulos[linha][coluna]) {

                    possiveisPulos = new boolean[8][8];

                    if (pecaSelecionada.isDama) {
                        checkJumpsDama();
                    } else {
                        checkJumps();
                    }

                    if (possiveisPulos[linha][coluna]) {
                        eatPeca((linha + pecaSelecionada.getY()) / 2, (coluna + pecaSelecionada.getX()) / 2);

                        movePeca(linha, coluna, true);
                    }
                } else {
                    pecaSelecionada = null;
                    possiveisPulos = new boolean[8][8];
                }
            }

            /*if (pecaSelecionada != null && (celulaSelecionada.peca == null)) {
                if (checkMove(linha, coluna)) {
                    puloMulti = false;
                    movePeca(linha, coluna, false);
                } else if (possiveisPulos[linha][coluna]) {
                    eatPeca((linha + pecaSelecionada.getY()) / 2, (coluna + pecaSelecionada.getX()) / 2);
                    movePeca(linha, coluna,true);
                } else {
                    pecaSelecionada = null;
                    possiveisPulos = new boolean[8][8];
                }
            }*/

            repaint();
        }
    }

    private boolean haPulos() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j].peca != null && tabuleiro[i][j].peca.cor == vez) {
                    if (tabuleiro[i][j].peca.isDama) {
                        checkJumpsDama(i, j);
                    } else {
                        checkJumps(i, j);
                    }
                }
            }
        }

        // puloMulti = false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (possiveisPulos[i][j]) {
                    System.out.println("Pulo");
                    return true;
                }
            }
        }

        System.out.println("Não pulo");
        return false;
    }
}

/*(((linha == pecaSelecionada.getY() + 1 && pecaSelecionada.cor == Peca.Cor.BRANCO) || linha == pecaSelecionada.getY() - 1) &&
                        ((coluna == pecaSelecionada.getX() + 1 && pecaSelecionada.cor == Peca.Cor.BRANCO)) || coluna == pecaSelecionada.getX() - 1))*/