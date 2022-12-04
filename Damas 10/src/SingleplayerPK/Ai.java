package SingleplayerPK;

import java.util.ArrayList;

public class Ai {

    public static Peca[][] getAiMovement(Peca[][] tabuleiro) {
        ArrayList<Peca[][]> tabuleiros = getAllBoards(tabuleiro, Peca.Cor.PRETO);

        int bestBoard = 0;
        int bestBoardValue = Integer.MAX_VALUE;

        for (Peca[][] tab : tabuleiros) {
            int score = minimax(tab, 5, true, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (score < bestBoardValue) {
                bestBoardValue = score;
                bestBoard = tabuleiros.indexOf(tab);
            }
        }

        try {
            return tabuleiros.get(bestBoard);
        }
        catch (Exception e) {
            return tabuleiro;
        }
    }

    private static int minimax(Peca[][] tabuleiro, int depth, boolean isMaximizing, int alpha, int beta) {
        if (depth == 0 || Tabuleiro.terminou(tabuleiro)) {
            return scoreTabuleiro(tabuleiro);
        }

        int bestScore;
        ArrayList<Peca[][]> tabuleiros;

        if (isMaximizing) {
            bestScore = Integer.MIN_VALUE;

            tabuleiros = getAllBoards(tabuleiro, Peca.Cor.PRETO);

            for (Peca[][] tabuleiroAtual : tabuleiros) {
                int score = minimax(tabuleiroAtual, depth - 1, false, alpha, beta);
                bestScore = Math.max(score, bestScore);
                alpha = Math.max(alpha, score);

                if (beta <= alpha) {
                    break;
                }
            }
        }

        // Ai:
        else {
            bestScore = Integer.MAX_VALUE;

            tabuleiros = getAllBoards(tabuleiro, Peca.Cor.BRANCO);

            for (Peca[][] tabuleiroAtual : tabuleiros) {
                int score = minimax(tabuleiroAtual, depth - 1, true, alpha, beta);
                bestScore = Math.min(score, bestScore);
                beta = Math.min(beta, score);

                if (beta <= alpha) {
                    break;
                }
            }
        }

        return bestScore;
    }

    private static ArrayList<Peca[][]> getAllBoards(Peca[][] tabuleiro, Peca.Cor cor) {

        ArrayList<Peca[][]> tabuleiros = new ArrayList<>();

        for (Peca[] linha : tabuleiro) {
            for (Peca peca : linha) {
                if (peca != null && peca.cor == cor) {
                    byte[][] possiveisMovimentos = Tabuleiro.verificarMovimentos(peca, tabuleiro, false);

                    if (possiveisMovimentos != null) {
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (possiveisMovimentos[i][j] > 0) {
                                    if (possiveisMovimentos[i][j] == 1) {
                                        Peca[][] tabuleiroMomentaneo = Tabuleiro.clone(tabuleiro);

                                        tabuleiroMomentaneo[i][j] = tabuleiroMomentaneo[peca.getY()][peca.getX()];
                                        tabuleiroMomentaneo[peca.getY()][peca.getX()] = null;

                                        tabuleiroMomentaneo[i][j].setX(j);
                                        tabuleiroMomentaneo[i][j].setY(i);

                                        tabuleiros.add(tabuleiroMomentaneo);
                                    }

                                    // Come peça:
                                    else if (possiveisMovimentos[i][j] == 2) {
                                        Peca[][] tabuleiroMomentaneo = Tabuleiro.clone(tabuleiro);

                                        tabuleiroMomentaneo = Tabuleiro.comerPeca(peca, tabuleiroMomentaneo, i, j);

                                        tabuleiroMomentaneo[i][j] = tabuleiroMomentaneo[peca.getY()][peca.getX()];
                                        tabuleiroMomentaneo[peca.getY()][peca.getX()] = null;

                                        tabuleiroMomentaneo[i][j].setX(j);
                                        tabuleiroMomentaneo[i][j].setY(i);

                                        tabuleiros.add(tabuleiroMomentaneo);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return tabuleiros;
    }

    private static int scoreTabuleiro(Peca[][] tabuleiro) {
        int brancas = 0;
        int pretas = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j] != null && tabuleiro[i][j].cor == Peca.Cor.BRANCO) {
                    brancas++;
                    if (tabuleiro[i][j].isDama)
                        brancas++;
                } else if (tabuleiro[i][j] != null && tabuleiro[i][j].cor == Peca.Cor.PRETO) {
                    pretas++;
                    if (tabuleiro[i][j].isDama)
                        pretas++;
                }
            }
        }

        return brancas - pretas;
    }
}