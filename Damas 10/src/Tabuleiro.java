import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Tabuleiro {

    ObjectInputStream receptor;
    ObjectOutputStream transmissor;

    Tela cliente1, cliente2;

    public Tabuleiro() throws IOException {
        startGame();

        cliente1.tabuleiro = this.tabuleiro;
        cliente2.tabuleiro = this.tabuleiro;

        this.cliente1.repaint();
        this.cliente2.repaint();
    }

    Peca[][] tabuleiro = new Peca[8][8];
    Peca pecaSelecionada;

    boolean puloMulti = false;
    byte[][] possiveisMovimentos = new byte[8][8]; // 0 = não pode mover, 1 = pode mover, 2 = comer

    Peca.Cor vez = Peca.Cor.BRANCO;

    int numBrancas = 12;
    int numPretas = 12;

    private void startGame() throws IOException {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tabuleiro[i][j] = null;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                tabuleiro[i][(j * 2 + (i % 2))] = new Peca((j * 2 + (i % 2)), i, Peca.Cor.PRETO);
            }
        }

        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                tabuleiro[i][j * 2 + (i % 2)] = new Peca((j * 2 + (i % 2)), i, Peca.Cor.BRANCO);
            }
        }
    }

    // ========================= MÉTODOS JOGO ========================= //

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
    public byte[][] verificarMovimentos() {
        if (pecaSelecionada != null) {

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

        return possiveisMovimentos;
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
}
