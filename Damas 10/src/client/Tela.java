package client;

import mensagens.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import javax.swing.*;

public class Tela extends JPanel implements ClientListener {
    Peca[][] tabuleiro = new Peca[8][8];
    Peca pecaSelecionada;

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = SCREEN_HEIGHT / 8;

    Cor cliente;
    Cor corContraria;
    Cor vez;

    byte[][] possiveisMovimentos = new byte[8][8]; // 0 = não pode mover, 1 = pode mover, 2 = comer

    boolean invert;
    private Client client;
    boolean jogoComecou = false;
    public Tela(){
        client = new Client(this, "localhost", 12345);
        while (!jogoComecou)
        {
            System.out.println("Aguardando outro cliente");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Cor recebida: " + cliente);
        if(cliente == Cor.BRANCO)
            corContraria = Cor.PRETO;
        else
            corContraria = Cor.BRANCO;


        vez = Cor.BRANCO;

        invert = cliente == Cor.PRETO;

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addMouseListener(new MyMouseAdapter());
        this.addMouseMotionListener(new MyMouseAdapter());

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tabuleiro[i][j] = null;
            }
        }

        invert = cliente == Cor.PRETO;

        startGame();
    }

    private void startGame(){
        numPretas = 13;
        numBrancas = 13;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                tabuleiro[i][(j * 2 + (i % 2))] = new Peca((j * 2 + (i % 2)), i, Cor.PRETO);
            }
        }

        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                tabuleiro[i][j * 2 + (i % 2)] = new Peca((j * 2 + (i % 2)), i, Cor.BRANCO);
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics graphics) throws IOException {
        Graphics2D g = (Graphics2D) graphics;
        g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
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
                    tabuleiro[i][j].draw(g, invert);
                }
            }
        }

        if (pecaSelecionada != null) {
            pecaSelecionada.drawCirc(g);
        }

        g.setColor(new Color(255, 228, 228, 100));

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

    // Seleciona a peça:
    public void selecionarPeca(int linha, int coluna) {
        if (tabuleiro[linha][coluna] != null && tabuleiro[linha][coluna].cor == cliente && vez == cliente)
            pecaSelecionada = tabuleiro[linha][coluna];
    }

    @Override
    public void receiveData(Mensagem msg) {
        if (msg instanceof MensagemInicioDeJogo)
        {
            MensagemInicioDeJogo msgInicio = (MensagemInicioDeJogo) msg;
            cliente = msgInicio.getCor();
            jogoComecou = true;
        }
        else if (msg instanceof MensagemTrocarVez)
        {
            MensagemTrocarVez msgTrocarVez = (MensagemTrocarVez) msg;
            vez = msgTrocarVez.getVez();
        }
        else if (msg instanceof MensagemSincronizarTabuleiro)
        {
            MensagemSincronizarTabuleiro msgSincronizar = (MensagemSincronizarTabuleiro) msg;
            this.tabuleiro = msgSincronizar.getTabuleiro();
            repaint();
        }
        else if (msg instanceof MensagemFimDeJogo)
        {
            MensagemFimDeJogo msgFimDeJogo = (MensagemFimDeJogo) msg;
            JOptionPane.showMessageDialog(null, "O time " + msgFimDeJogo.getVencedor() + " venceu!");
            possiveisMovimentos = new byte[8][8];
            tabuleiro = new Peca[8][8];
            startGame();
        }
    }
    boolean puloMulti = false;
    public byte[][] verificarMovimentos() {
        possiveisMovimentos = new byte[8][8];
        if (pecaSelecionada != null) {

            int loops = pecaSelecionada.isDama ? 8 : 1;

            int linha = pecaSelecionada.getY();
            int coluna = pecaSelecionada.getX();

            if (!pecaSelecionada.isDama) {
                if (pecaSelecionada.cor == Cor.PRETO || (pecaSelecionada.cor == Cor.BRANCO && puloMulti)) {
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

                if (pecaSelecionada.cor == Cor.BRANCO || (pecaSelecionada.cor == Cor.PRETO && puloMulti)) {
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
    int numBrancas = 13;
    int numPretas = 13;
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

        if (pecaSelecionada.cor == Cor.PRETO) {
            numBrancas--;
        } else {
            numPretas--;
        }
        if (numBrancas == 0)
        {
            client.sendData(new MensagemFimDeJogo(Cor.PRETO));
        }
        else if (numPretas == 0)
        {
            client.sendData(new MensagemFimDeJogo(Cor.BRANCO));
        }
    }
    private void tornarDama(int linha) {
        if (pecaSelecionada.cor == Cor.BRANCO && linha == 0) {
            pecaSelecionada.isDama = true;
        } else if (pecaSelecionada.cor == Cor.PRETO && linha == 7) {
            pecaSelecionada.isDama = true;
        }
    }
    public void moverPeca(int linha, int coluna, boolean pulo) {
        if (linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8) {
            if (tabuleiro[linha][coluna] == null) {
                tabuleiro[pecaSelecionada.getY()][pecaSelecionada.getX()] = null;
                pecaSelecionada.setY(linha);
                pecaSelecionada.setX(coluna);
                tornarDama(linha);
                tabuleiro[linha][coluna] = pecaSelecionada;

                if (pulo) {
                    puloMulti = true;

                    verificarMovimentos();

                    if (!verificarPulos()) {
                        pecaSelecionada = null;
                        possiveisMovimentos = new byte[8][8];
                        vez = vez == Cor.BRANCO ? Cor.PRETO : Cor.BRANCO;
                        puloMulti = false;
                    }
                    client.sendData(new MensagemSincronizarTabuleiro(tabuleiro));
                } else {
                    pecaSelecionada = null;
                    possiveisMovimentos = new byte[8][8];
                    vez = vez == Cor.BRANCO ? Cor.PRETO : Cor.BRANCO;
                }
            }
        }
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
                client.sendData(new MensagemSincronizarTabuleiro(tabuleiro));
                pecaSelecionada = null;
                possiveisMovimentos = new byte[8][8];
            }

            // Movimenta a peça caso o movimento seja válido e COME uma peça:
            else if (possiveisMovimentos[linha][coluna] == 2) {
                comerPeca(linha, coluna);
                moverPeca(linha, coluna, true);
                client.sendData(new MensagemSincronizarTabuleiro(tabuleiro));
            }


            // Se o movimento não for válido, verifica se outra peça foi selecionada:
            else if (tabuleiro[linha][coluna] != null && tabuleiro[linha][coluna].cor == vez) {
                if (!puloMulti) {
                    selecionarPeca(linha, coluna);
                    verificarMovimentos();
                    client.sendData(new MensagemSincronizarTabuleiro(tabuleiro));
                }
            }


            // Se o movimento não for válido e outra peça não foi selecionada, deseleciona a peça (sempre acaba aqui):
            else {
                pecaSelecionada = null;
                possiveisMovimentos = new byte[8][8];

                if (puloMulti) {
                    puloMulti = false;
                    vez = vez == Cor.BRANCO ? Cor.PRETO : Cor.BRANCO;
                }
            }
            client.sendData(new MensagemTrocarVez(vez));
            repaint();
        }
    }
}
