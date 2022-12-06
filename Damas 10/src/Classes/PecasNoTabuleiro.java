package Classes;

import Cliente.Peca;

public class PecasNoTabuleiro {
    private Peca[][] pecas;
    public PecasNoTabuleiro(Peca[][] tabuleiro)
    {
        pecas = tabuleiro;
    }
    public Peca getPeca(int linha, int coluna) throws Exception
    {
        if (linha > pecas.length || linha < 0) throw new Exception("Linha inválida");
        if (coluna > pecas[linha].length || coluna < 0) throw new Exception("Coluna inválida");
        return pecas[linha][coluna];
    }
    public Peca[][] getPecas()
    {
        return pecas;
    }
}
