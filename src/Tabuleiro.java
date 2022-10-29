public class Tabuleiro {
    private Peca[][] pecas;
    private Peca pecaSeleciona = null;
    private EnumCor vez = EnumCor.PRETO;

    public Tabuleiro(){
        this.pecas = new Peca[8][8];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                Peca peca = new Peca(j * 2 + (i % 2), i, EnumCor.BRANCO);
                this.adicionaPeca(peca);
            }
        }

        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                Peca peca = new Peca(j * 2 + (i % 2), i, EnumCor.PRETO);
                this.adicionaPeca(peca);
            }
        }
    }

    public Peca getPeca(int linha, int coluna){
        return pecas[linha][coluna];
    }
    public void adicionaPeca(Peca peca){
        pecas[peca.getX()][peca.getY()] = peca;
        peca.setTabuleiro(this);
    }
    public void selecionaPeca(Peca peca){
        if(peca.isSelecionada()){
            peca.setSelecionada(false);
            pecaSeleciona = null;
        }else{
            peca.setSelecionada(true);
            pecaSeleciona = peca;
        }
    }

    public void movePeca(Peca peca, int linha, int coluna){
        if(peca.validacaoDeMovimento(linha, coluna)){
            pecas[peca.getX()][peca.getY()] = null;
            peca.setX(linha);
            peca.setY(coluna);
            pecas[linha][coluna] = peca;
        }
    }
    public void inverteVez(){
        if(vez.equals(EnumCor.PRETO)){
            vez = vez.BRANCO;
        }else{
            vez = vez.PRETO;
        }
    }

    public void realizaJogada(int linha, int coluna) {
        System.out.println("Linha: " + linha + " Coluna: " + coluna);
        Peca peca = this.getPeca(linha, coluna);
        if(this.pecaSeleciona == null) {
            System.out.println(peca.getCor());
            if (peca != null && peca.getCor().equals(vez)) {
                this.selecionaPeca(peca);
            }
        }else{
            if(peca == null){
                this.movePeca(pecaSeleciona, linha, coluna);
                this.selecionaPeca(pecaSeleciona);
            }else{
                if(this.pecaSeleciona == peca){
                    this.selecionaPeca(peca);
                }if(peca == null || !peca.getCor().equals(this.pecaSeleciona.getCor())){
                    this.movePeca(this.pecaSeleciona, linha, coluna);
                }else{
                    this.selecionaPeca(pecaSeleciona);
                }
            }
        }
    }
}
