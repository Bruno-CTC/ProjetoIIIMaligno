package SingleplayerPK;

public class Tabuleiro {

    public static byte[][] verificarMovimentos(Peca peca, Peca[][] tabuleiro, boolean puloMulti) {
        byte[][] possiveisMovimentos = null;

        if (peca != null) {

            int loops = 8;

            int linha = peca.getY();
            int coluna = peca.getX();

            if (!peca.isDama) {
                if (peca.cor == Peca.Cor.PRETO || (peca.cor == Peca.Cor.BRANCO && puloMulti)) {
                    if (linha + 1 < 8 && coluna + 1 < 8) {
                        if (tabuleiro[linha + 1][coluna + 1] == null) {
                            if (!puloMulti) {
                                if (possiveisMovimentos == null)
                                    possiveisMovimentos = new byte[8][8];
                                possiveisMovimentos[linha + 1][coluna + 1] = 1;
                            }
                        } else if (tabuleiro[linha + 1][coluna + 1].cor != peca.cor &&
                                linha + 2 < 8 && coluna + 2 < 8
                                && tabuleiro[linha + 2][coluna + 2] == null) {

                            if (possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha + 2][coluna + 2] = 2;
                        }
                    }

                    if (linha + 1 < 8 && coluna - 1 >= 0) {
                        if (tabuleiro[linha + 1][coluna - 1] == null) {
                            if (!puloMulti) {
                                if (possiveisMovimentos == null)
                                    possiveisMovimentos = new byte[8][8];
                                possiveisMovimentos[linha + 1][coluna - 1] = 1;
                            }
                        } else if (tabuleiro[linha + 1][coluna - 1].cor != peca.cor &&
                                linha + 2 < 8 && coluna - 2 >= 0
                                && tabuleiro[linha + 2][coluna - 2] == null) {

                            if (possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha + 2][coluna - 2] = 2;
                        }
                    }
                }

                if (peca.cor == Peca.Cor.BRANCO || (peca.cor == Peca.Cor.PRETO && puloMulti)) {
                    if (linha - 1 >= 0 && coluna + 1 < 8) {
                        if (tabuleiro[linha - 1][coluna + 1] == null) {
                            if (!puloMulti) {
                                possiveisMovimentos = new byte[8][8];
                                possiveisMovimentos[linha - 1][coluna + 1] = 1;
                            }
                        } else if (tabuleiro[linha - 1][coluna + 1].cor != peca.cor &&
                                linha - 2 >= 0 && coluna + 2 < 8
                                && tabuleiro[linha - 2][coluna + 2] == null) {

                            if(possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha - 2][coluna + 2] = 2;
                        }
                    }

                    if (linha - 1 >= 0 && coluna - 1 >= 0) {
                        if (tabuleiro[linha - 1][coluna - 1] == null) {
                            if (!puloMulti) {
                                if(possiveisMovimentos == null)
                                    possiveisMovimentos = new byte[8][8];
                                possiveisMovimentos[linha - 1][coluna - 1] = 1;
                            }
                        } else if (tabuleiro[linha - 1][coluna - 1].cor != peca.cor &&
                                linha - 2 >= 0 && coluna - 2 >= 0
                                && tabuleiro[linha - 2][coluna - 2] == null) {

                            if(possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
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
                            if(possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha + i][coluna + i] = valor;
                        }

                        // Verifica se a casa está ocupada por uma peça do mesmo time:
                        else if (tabuleiro[linha + i][coluna + i].cor == peca.cor) {
                            break;
                        }

                        // Verifica se a casa tem uma peça de cor diferente:
                        else if (tabuleiro[linha + i][coluna + i].cor != peca.cor &&
                                linha + i + 1 < 8 && coluna + i + 1 < 8
                                && tabuleiro[linha + i + 1][coluna + i + 1] == null && valor == 1) {

                            valor = 2;
                            if(possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha + i + 1][coluna + i + 1] = valor;
                        }
                    }
                    else {
                        break;
                    }
                }

                for (int i = 1; i < loops; i++) {
                    if (linha + i < 8 && coluna - i >= 0) {
                        // Verifica se a casa está livre:
                        if (tabuleiro[linha + i][coluna - i] == null) {
                            if(possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha + i][coluna - i] = valor;
                        }

                        // Verifica se a casa está ocupada por uma peça do mesmo time:
                        else if (tabuleiro[linha + i][coluna - i].cor == peca.cor) {
                            break;
                        }

                        // Verifica se a casa tem uma peça de cor diferente:
                        else if (tabuleiro[linha + i][coluna - i].cor != peca.cor &&
                                linha + i + 1 < 8 && coluna - i - 1 >= 0
                                && tabuleiro[linha + i + 1][coluna - i - 1] == null && valor == 1) {

                            valor = 2;
                            if(possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha + i + 1][coluna - i - 1] = valor;
                        }
                    }
                    else {
                        break;
                    }
                }
                for (int i = 1; i < loops; i++) {

                    if (linha - i >= 0 && coluna + i < 8) {

                        // Verifica se a casa está livre:
                        if (tabuleiro[linha - i][coluna + i] == null) {
                            if(possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha - i][coluna + i] = valor;
                        }

                        // Verifica se a casa está ocupada por uma peça do mesmo time:
                        else if (tabuleiro[linha - i][coluna + i].cor == peca.cor) {
                            break;
                        }

                        // Verifica se a casa tem uma peça de cor diferente:
                        else if (tabuleiro[linha - i][coluna + i].cor != peca.cor &&
                                linha - i - 1 >= 0 && coluna + i + 1 < 8
                                && tabuleiro[linha - i - 1][coluna + i + 1] == null && valor == 1) {

                            valor = 2;
                            if(possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha - i - 1][coluna + i + 1] = valor;
                        }
                    }
                    else {
                        break;
                    }
                }
                for (int i = 1; i < loops; i++) {
                    if (linha - i >= 0 && coluna - i >= 0) {

                        // Verifica se a casa está livre:
                        if (tabuleiro[linha - i][coluna - i] == null) {
                            if(possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha - i][coluna - i] = valor;
                        }

                        // Verifica se a casa está ocupada por uma peça do mesmo time:
                        else if (tabuleiro[linha - i][coluna - i].cor == peca.cor) {
                            break;
                        }

                        // Verifica se a casa tem uma peça de cor diferente:
                        else if (tabuleiro[linha - i][coluna - i].cor != peca.cor &&
                                linha - i - 1 >= 0 && coluna - i - 1 >= 0
                                && tabuleiro[linha - i - 1][coluna - i - 1] == null && valor == 1) {

                            valor = 2;
                            if(possiveisMovimentos == null)
                                possiveisMovimentos = new byte[8][8];
                            possiveisMovimentos[linha - i - 1][coluna - i - 1] = valor;
                        }
                    }
                    else {
                        break;
                    }
                }
            }
        }

        return possiveisMovimentos;
    }


    public static Peca[][] comerPeca(Peca pecaSelecionada, Peca[][] tabuleiroOG, int linha, int coluna) {

        Peca[][] tabuleiro = Tabuleiro.clone(tabuleiroOG);

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

        return tabuleiro;
    }


    public static Peca[][] clone(Peca[][] tabuleiro) {

        Peca[][] ret = new Peca[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j] != null) {
                    ret[i][j] = tabuleiro[i][j].clone();
                }
            }
        }

        return ret;
    }

    public static boolean terminou(Peca[][] tabuleiro) {

        int brancas = 0;
        int pretas = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j] != null) {
                    if (tabuleiro[i][j].cor == Peca.Cor.BRANCO) {
                        brancas++;
                    } else {
                        pretas++;
                    }
                }
            }
        }

        return brancas == 0 || pretas == 0;
    }
}