package Cliente;

import Classes.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Tela extends JPanel implements ActionListener {
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
    private Parceiro servidor;
    public Tela(Parceiro p){
        this.servidor = p;
        try {
            servidor.receba(new PedidoCor());
        } catch (Exception e) {

        }
        Comunicado comunicado = null;
        do {
            try{
                comunicado = (Comunicado) servidor.espie();
            }catch (Exception e){}
        }while(!(comunicado instanceof ComunicadoDeCOR));
        ComunicadoDeCOR comCor = (ComunicadoDeCOR) comunicado;
        System.out.println(comCor);
        System.out.println(comCor.getCor());
        cliente = comCor.getCor();
        if(cliente == Cor.BRANCO)
            corContraria = Cor.PRETO;
        else
            corContraria = Cor.BRANCO;


        vez = Cor.BRANCO;

        invert = false;

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

        //invert = cliente == Peca.Cor.PRETO;

        startGame();
    }

    private void startGame(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                tabuleiro[i][(j * 2 + (i % 2))] = new Peca((j * 2 + (i % 2)), i, corContraria);
            }
        }

        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                tabuleiro[i][j * 2 + (i % 2)] = new Peca((j * 2 + (i % 2)), i, cliente);
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
                if (tabuleiro[i][j] != null && !tabuleiro[i][j].isDama) {
                    tabuleiro[i][j].draw(g, invert);
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
            /*if (pecaSelecionada == null) {
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
            }*/


            repaint();
        }
    }
}
