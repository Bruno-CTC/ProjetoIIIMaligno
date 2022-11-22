import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;

public class GamePanel1 extends JPanel implements ActionListener {

    Peca[][] tabuleiro = new Peca[8][8];
    Peca pecaSelecionada;

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = SCREEN_HEIGHT / 8;

    Peca.Cor cliente;
    boolean puloMulti = false;
    byte[][] possiveisMovimentos = new byte[8][8]; // 0 = não pode mover, 1 = pode mover, 2 = comer

    Peca.Cor vez = Peca.Cor.BRANCO;

    int numBrancas = 12;
    int numPretas = 12;

    boolean invert = false;

    public GamePanel1(Peca.Cor cliente) throws IOException {
        this.cliente = cliente;

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addMouseListener(new GamePanel1.MyMouseAdapter());
        this.addMouseMotionListener(new GamePanel1.MyMouseAdapter());

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tabuleiro[i][j] = null;
            }
        }

        invert = cliente == Peca.Cor.PRETO;

        startGame();
    }

    private void startGame() throws IOException {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                tabuleiro[i][(j * 2 + (i % 2))] = new Peca((j * 2 + (i % 2)), i, Peca.Cor.PRETO, invert);
            }
        }

        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                tabuleiro[i][j * 2 + (i % 2)] = new Peca((j * 2 + (i % 2)), i, Peca.Cor.BRANCO, invert);
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
                if (tabuleiro[i][j] != null) {
                    tabuleiro[i][j].draw(g);
                }
            }
        }

        if (pecaSelecionada != null) {
            pecaSelecionada.drawCirc(g);
        }

        // Desenha as damas:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j] != null) {
                    if (tabuleiro[i][j].isDama) {
                        tabuleiro[i][j].drawDama(g);
                    }
                }
            }
        }

        // Desenha possíveis pulos:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (possiveisMovimentos[i][j] > 0) {
                    g.setColor(new Color(255, 228, 228, 100));
                    if (invert) {
                        g.fillOval((7 - j) * UNIT_SIZE + UNIT_SIZE / 4 + 5 / 2, (7 - i) * UNIT_SIZE + UNIT_SIZE / 4 + 5 / 2,
                                UNIT_SIZE / 2 - 5, UNIT_SIZE / 2 - 5);
                    } else {
                        g.fillOval((j) * UNIT_SIZE + UNIT_SIZE / 4 + 5 / 2, (i) * UNIT_SIZE + UNIT_SIZE / 4 + 5 / 2,
                                UNIT_SIZE / 2 - 5, UNIT_SIZE / 2 - 5);
                    }
                }
            }
        }
    }

    // ============================= MÉTODOS JOGO ============================= //

    // Seleciona a peça:
    public void selecionarPeca(int linha, int coluna) {
        if (tabuleiro[linha][coluna] != null && tabuleiro[linha][coluna].cor == vez)
            pecaSelecionada = tabuleiro[linha][coluna];
    }

    // Move a peça:
    public void moverPeca(int linha, int coluna, boolean pulo) {
        if (linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8) {
            if (tabuleiro[linha][coluna] == null) {
                tabuleiro[pecaSelecionada.getY()][pecaSelecionada.getX()] = null;
                tabuleiro[linha][coluna] = pecaSelecionada;
                pecaSelecionada.setY(linha);
                pecaSelecionada.setX(coluna);

                tornarDama(linha);

                if (pulo) {
                    puloMulti = true;

                    verificarMovimentos();

                    if (!verificarPulos()) {
                        pecaSelecionada = null;
                        possiveisMovimentos = new byte[8][8];
                        vez = vez == Peca.Cor.BRANCO ? Peca.Cor.PRETO : Peca.Cor.BRANCO;
                        puloMulti = false;
                    }
                } else {
                    pecaSelecionada = null;
                    possiveisMovimentos = new byte[8][8];
                    vez = vez == Peca.Cor.BRANCO ? Peca.Cor.PRETO : Peca.Cor.BRANCO;
                }
            }
        }
    }

    // Verifica todos os movimentos possíveis e pulos, e armazena na matriz possiveisMovimentos:
    public void verificarMovimentos() {
        if (pecaSelecionada != null) {
            possiveisMovimentos = new byte[8][8];

            int loops = pecaSelecionada.isDama ? 8 : 1;

            int linha = pecaSelecionada.getY();
            int coluna = pecaSelecionada.getX();

            if (!pecaSelecionada.isDama) {
                if (pecaSelecionada.cor == Peca.Cor.PRETO || (pecaSelecionada.cor == Peca.Cor.BRANCO && puloMulti)) {
                    if (linha + 1 < 8 && coluna + 1 < 8) {
                        if (tabuleiro[linha + 1][coluna + 1] == null) {
                            if (!puloMulti)
                                possiveisMovimentos[linha + 1][coluna + 1] = 1;
                        } else if (tabuleiro[linha + 1][coluna + 1].cor != pecaSelecionada.cor &&
                                linha + 2 < 8 && coluna + 2 < 8
                                && tabuleiro[linha + 2][coluna + 2] == null) {

                            possiveisMovimentos[linha + 2][coluna + 2] = 2;
                        }
                    }

                    if (linha + 1 < 8 && coluna - 1 >= 0) {
                        if (tabuleiro[linha + 1][coluna - 1] == null) {
                            if (!puloMulti)
                                possiveisMovimentos[linha + 1][coluna - 1] = 1;
                        } else if (tabuleiro[linha + 1][coluna - 1].cor != pecaSelecionada.cor &&
                                linha + 2 < 8 && coluna - 2 >= 0
                                && tabuleiro[linha + 2][coluna - 2] == null) {

                            possiveisMovimentos[linha + 2][coluna - 2] = 2;
                        }
                    }
                }

                if (pecaSelecionada.cor == Peca.Cor.BRANCO || (pecaSelecionada.cor == Peca.Cor.PRETO && puloMulti)) {
                    if (linha - 1 >= 0 && coluna + 1 < 8) {
                        if (tabuleiro[linha - 1][coluna + 1] == null) {
                            if (!puloMulti)
                                possiveisMovimentos[linha - 1][coluna + 1] = 1;
                        } else if (tabuleiro[linha - 1][coluna + 1].cor != pecaSelecionada.cor &&
                                linha - 2 >= 0 && coluna + 2 < 8
                                && tabuleiro[linha - 2][coluna + 2] == null) {

                            possiveisMovimentos[linha - 2][coluna + 2] = 2;
                        }
                    }

                    if (linha - 1 >= 0 && coluna - 1 >= 0) {
                        if (tabuleiro[linha - 1][coluna - 1] == null) {
                            if (!puloMulti)
                                possiveisMovimentos[linha - 1][coluna - 1] = 1;
                        } else if (tabuleiro[linha - 1][coluna - 1].cor != pecaSelecionada.cor &&
                                linha - 2 >= 0 && coluna - 2 >= 0
                                && tabuleiro[linha - 2][coluna - 2] == null) {

                            possiveisMovimentos[linha - 2][coluna - 2] = 2;
                        }
                    }
                }
            } else {

                byte valor = 1;

                for (int i = 1; i < loops; i++) {
                    if (linha + i < 8 && coluna + i < 8) {
                        // Verifica se a casa está livre:
                        if (tabuleiro[linha + i][coluna + i] == null) {
                            possiveisMovimentos[linha + i][coluna + i] = valor;
                        }

                        // Verifica se a casa está ocupada por uma peça do mesmo time:
                        else if (tabuleiro[linha + i][coluna + i].cor == pecaSelecionada.cor) {
                            break;
                        }

                        // Verifica se a casa tem uma peça de cor diferente:
                        else if (tabuleiro[linha + i][coluna + i].cor != pecaSelecionada.cor &&
                                linha + i + 1 < 8 && coluna + i + 1 < 8
                                && tabuleiro[linha + i + 1][coluna + i + 1] == null && valor == 1) {

                            valor = 2;
                            possiveisMovimentos[linha + i + 1][coluna + i + 1] = valor;
                        }
                    }
                }

                for (int i = 1; i < loops; i++) {
                    if (linha + i < 8 && coluna - i >= 0) {
                        // Verifica se a casa está livre:
                        if (tabuleiro[linha + i][coluna - i] == null) {
                            possiveisMovimentos[linha + i][coluna - i] = valor;
                        }

                        // Verifica se a casa está ocupada por uma peça do mesmo time:
                        else if (tabuleiro[linha + i][coluna - i].cor == pecaSelecionada.cor) {
                            break;
                        }

                        // Verifica se a casa tem uma peça de cor diferente:
                        else if (tabuleiro[linha + i][coluna - i].cor != pecaSelecionada.cor &&
                                linha + i + 1 < 8 && coluna - i - 1 >= 0
                                && tabuleiro[linha + i + 1][coluna - i - 1] == null && valor == 1) {

                            valor = 2;
                            possiveisMovimentos[linha + i + 1][coluna - i - 1] = valor;
                        }
                    }
                }
                for (int i = 1; i < loops; i++) {

                    if (linha - i >= 0 && coluna + i < 8) {

                        // Verifica se a casa está livre:
                        if (tabuleiro[linha - i][coluna + i] == null) {
                            possiveisMovimentos[linha - i][coluna + i] = valor;
                        }

                        // Verifica se a casa está ocupada por uma peça do mesmo time:
                        else if (tabuleiro[linha - i][coluna + i].cor == pecaSelecionada.cor) {
                            break;
                        }

                        // Verifica se a casa tem uma peça de cor diferente:
                        else if (tabuleiro[linha - i][coluna + i].cor != pecaSelecionada.cor &&
                                linha - i - 1 >= 0 && coluna + i + 1 < 8
                                && tabuleiro[linha - i - 1][coluna + i + 1] == null && valor == 1) {

                            valor = 2;
                            possiveisMovimentos[linha - i - 1][coluna + i + 1] = valor;
                        }
                    }
                }
                for (int i = 1; i < loops; i++) {
                    if (linha - i >= 0 && coluna - i >= 0) {

                        // Verifica se a casa está livre:
                        if (tabuleiro[linha - i][coluna - i] == null) {
                            possiveisMovimentos[linha - i][coluna - i] = valor;
                        }

                        // Verifica se a casa está ocupada por uma peça do mesmo time:
                        else if (tabuleiro[linha - i][coluna - i].cor == pecaSelecionada.cor) {
                            break;
                        }

                        // Verifica se a casa tem uma peça de cor diferente:
                        else if (tabuleiro[linha - i][coluna - i].cor != pecaSelecionada.cor &&
                                linha - i - 1 >= 0 && coluna - i - 1 >= 0
                                && tabuleiro[linha - i - 1][coluna - i - 1] == null && valor == 1) {

                            valor = 2;
                            possiveisMovimentos[linha - i - 1][coluna - i - 1] = valor;
                        }
                    }
                }
            }
        }
    }

    // Método que verifica se o jogador pode realizar um pulo:
    private boolean verificarPulos() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (possiveisMovimentos[i][j] == 2) {
                    return true;
                }
            }
        }

        return false;
    }

    // Método que come a peça
    private void comerPeca(int linha, int coluna) {
        if (!pecaSelecionada.isDama) {
            tabuleiro[(linha + pecaSelecionada.getY()) / 2][(coluna + pecaSelecionada.getX()) / 2] = null;
        } else {
            int linhaPeca = pecaSelecionada.getY();
            int colunaPeca = pecaSelecionada.getX();

            if (linhaPeca < linha && colunaPeca < coluna) {
                for (int i = 1; i < linha - linhaPeca; i++) {
                    tabuleiro[linhaPeca + i][colunaPeca + i] = null;
                }
            } else if (linhaPeca < linha && colunaPeca > coluna) {
                for (int i = 1; i < linha - linhaPeca; i++) {
                    tabuleiro[linhaPeca + i][colunaPeca - i] = null;
                }
            } else if (linhaPeca > linha && colunaPeca < coluna) {
                for (int i = 1; i < linhaPeca - linha; i++) {
                    tabuleiro[linhaPeca - i][colunaPeca + i] = null;
                }
            } else if (linhaPeca > linha && colunaPeca > coluna) {
                for (int i = 1; i < linhaPeca - linha; i++) {
                    tabuleiro[linhaPeca - i][colunaPeca - i] = null;
                }
            }
        }

        if (pecaSelecionada.cor == Peca.Cor.PRETO) {
            numBrancas--;
        } else {
            numPretas--;
        }
    }

    // Método que verifica se a peça se tornou dama:
    private void tornarDama(int linha) {
        if (pecaSelecionada.cor == Peca.Cor.BRANCO && linha == 0) {
            pecaSelecionada.isDama = true;
        } else if (pecaSelecionada.cor == Peca.Cor.PRETO && linha == 7) {
            pecaSelecionada.isDama = true;
        }
    }

    // Método que verifica se o jogo acabou:
    private boolean verificarFimDeJogo() {
        return numBrancas == 0 || numPretas == 0;
    }

    // ============================= MÉTODOS MOUSE ============================= //

    public void actionPerformed(ActionEvent e) {
    }

    public class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {

            int linha = e.getY() / UNIT_SIZE;
            int coluna = e.getX() / UNIT_SIZE;

            if (invert) {
                linha = 7 - linha;
                coluna = 7 - coluna;
            }

            // ============================= MOVIMENTOS E SELEÇÕES ============================= //

            // Seleciona a peça:
            if (pecaSelecionada == null) {
                selecionarPeca(linha, coluna);
                verificarMovimentos();
            }

            // Verifica se a peça clicada ja estava selecionada:
            else if (pecaSelecionada == tabuleiro[linha][coluna]) {
                pecaSelecionada = null;
                possiveisMovimentos = new byte[8][8];
            }

            // Movimenta a peça caso o movimento seja válido:
            else if (possiveisMovimentos[linha][coluna] == 1) {
                moverPeca(linha, coluna, false);
                pecaSelecionada = null;
                possiveisMovimentos = new byte[8][8];
            }

            // Movimenta a peça caso o movimento seja válido e COME uma peça:
            else if (possiveisMovimentos[linha][coluna] == 2) {
                comerPeca(linha, coluna);
                moverPeca(linha, coluna, true);
            }


            // Se o movimento não for válido, verifica se outra peça foi selecionada:
            else if (tabuleiro[linha][coluna] != null &&
                    tabuleiro[linha][coluna].cor == vez) {

                if (!puloMulti) {
                    selecionarPeca(linha, coluna);
                    verificarMovimentos();
                }
            }


            // Se o movimento não for válido e outra peça não foi selecionada, deseleciona a peça (sempre acaba aqui):
            else {
                pecaSelecionada = null;
                possiveisMovimentos = new byte[8][8];

                if (puloMulti) {
                    puloMulti = false;
                    vez = vez == Peca.Cor.BRANCO ? Peca.Cor.PRETO : Peca.Cor.BRANCO;
                }
            }


            repaint();
        }
    }
}