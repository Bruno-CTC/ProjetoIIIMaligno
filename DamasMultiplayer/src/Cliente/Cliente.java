package Cliente;

import Classes.*;

import java.io.*;
import java.net.*;

public class Cliente {
    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 3000;

        public static void main(String[] args) {
            if (args.length > 2) {
                System.err.println("Uso esperado: java Cliente.Cliente [HOST [PORTA]]\n");
                return;
            }

            Socket conexao = null;
            try {
                String host = Cliente.HOST_PADRAO;
                int porta = Cliente.PORTA_PADRAO;

                if (args.length > 0)
                    host = args[0];

                if (args.length == 2)
                    porta = Integer.parseInt(args[1]);

                conexao = new Socket(host, porta);
            } catch (Exception erro) {
                System.err.println("Indique o servidor e a porta corretos!\n");
                return;
            }

            ObjectOutputStream transmissor = null;
            try {
                transmissor =
                        new ObjectOutputStream(
                                conexao.getOutputStream());
            } catch (Exception erro) {
                System.err.println("Indique o servidor e a porta corretos!\n");
                return;
            }

            ObjectInputStream receptor = null;
            try {
                receptor =
                        new ObjectInputStream(
                                conexao.getInputStream());
            } catch (Exception erro) {
                System.err.println("Indique o servidor e a porta corretos!\n");
                return;
            }

            Parceiro servidor = null;
            try {
                servidor =
                        new Parceiro(conexao, receptor, transmissor);
            } catch (Exception erro) {
                System.err.println("Indique o servidor e a porta corretos!\n");
                return;
            }

            try {
                new GameFrame(new Tela(servidor));
            }
            catch (Exception erro) {
                System.err.println("Erro ao gerar janela.\n");
                return;
            }

            TratadoraDeComunicadoDeDesligamento tratadoraDeComunicadoDeDesligamento = null;
            try {
                tratadoraDeComunicadoDeDesligamento = new TratadoraDeComunicadoDeDesligamento(servidor);
            } catch (Exception erro) {
            } // sei que servidor foi instanciado

            tratadoraDeComunicadoDeDesligamento.start();

        }
}
