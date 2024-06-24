package aeds3;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Criptografia {
    public static void main(String[] args)
    {
        byte[] teste = "abcdef".getBytes();
        String chave = "casa";

        byte[] arrayBytesCifrados = Cifrar(teste, chave);

        byte[] arrayBytesDecifrados = Decifrar(arrayBytesCifrados, chave);

        System.out.println("Mensagem inicial: " + new String(teste));
        System.out.println("Mensagem cifrada: " + new String(arrayBytesCifrados));
        System.out.println("Mensagem decifrada: " + new String(arrayBytesDecifrados));

    }

    public static byte[] Cifrar(byte[] dados, String chave)
    {
        byte[] dadosCifradosSubstituicao = CifrarPorSubstituicao(dados, chave);
        byte[] dadosCifradosTransposicao =  CifrarPorTransposicao(chave, dadosCifradosSubstituicao);

        return dadosCifradosTransposicao;
    }

    public static byte[] Decifrar(byte[] dadosCifrados, String chave)
    {
        byte[] dadosDecifradosTransposicao = DecifrarPorTransposicao(chave, dadosCifrados);
        byte[] dadosDecifradosSubstituicao = DecifrarPorSubstituicao(chave, dadosDecifradosTransposicao);

        return dadosDecifradosSubstituicao;
    }

    private static byte[] CifrarPorTransposicao(String chave, byte[] dadosCifrados) {
        int numColunas = chave.length();
        int numLinhas = (int) Math.ceil((double) dadosCifrados.length / numColunas);
        byte[][] matriz = new byte[numLinhas][numColunas];

        for (int i = 0; i < dadosCifrados.length; i++) {
            matriz[i / numColunas][i % numColunas] = dadosCifrados[i];
        }

        Integer[] ordemColunas = obterOrdemColunas(chave);

        byte[] dadosCifradosTransposicao = new byte[dadosCifrados.length];
        int index = 0;
        for (int col : ordemColunas) {
            for (int linha = 0; linha < numLinhas; linha++) {
                if (index < dadosCifrados.length && matriz[linha][col] != 0) {
                    dadosCifradosTransposicao[index++] = matriz[linha][col];
                }
            }
        }

        return dadosCifradosTransposicao;
    }

    private static byte[] CifrarPorSubstituicao(byte[] dados, String chave) 
    {
        int tamanhoChaveAlinhada = dados.length;
        StringBuilder chaveAlinhada = new StringBuilder(tamanhoChaveAlinhada);

        for (int i = 0; i < tamanhoChaveAlinhada; i++) 
        {
            chaveAlinhada.append(chave.charAt(i % chave.length()));
        }

        byte[] chaveAlinhadaEmBytes = chaveAlinhada.toString().getBytes();
        byte[] dadosCifrados = new byte[dados.length];

        for(int i = 0; i < tamanhoChaveAlinhada; i++)
        {
            dadosCifrados[i] = (byte)(dados[i] + chaveAlinhadaEmBytes[i]);
        }
        return dadosCifrados;
    }
    
    private static byte[] DecifrarPorTransposicao(String chave, byte[] dadosCifrados) 
    {
        Boolean chaveParMensagemImpar = (chave.length()%2 == 0) && (dadosCifrados.length%2 != 0);
        Boolean chaveImparMensagemImpar = (chave.length()%2 != 0) && (dadosCifrados.length%2 != 0);

        int numLinhasCompletas = dadosCifrados.length % chave.length();

        int numColunas = chave.length();
        int numLinhas = (int) Math.ceil((double) dadosCifrados.length / numColunas);
        byte[][] matriz = new byte[numLinhas][numColunas];

        Integer[] ordemColunas = obterOrdemColunas(chave);

        int index = 0;
        for (int col : ordemColunas) {
            if(chaveParMensagemImpar && col == chave.length() - 1)
            {
                for (int linha = 0; linha < numLinhas - 1; linha++) {
                    if (index < dadosCifrados.length) {
                        matriz[linha][col] = dadosCifrados[index++];
                    }
                }
                continue;
            }
            if(chaveImparMensagemImpar)
            {
                if(col != numLinhasCompletas - 1)
                {
                    for (int linha = 0; linha < numLinhas - 1; linha++) {
                        if (index < dadosCifrados.length) {
                            byte valor =  dadosCifrados[index++];
                            matriz[linha][col] = valor;
                        }
                    }
                    continue;
                }
                for (int linha = 0; linha < numLinhas; linha++) {
                    if (index < dadosCifrados.length) {
                        byte valor =  dadosCifrados[index++];
                        matriz[linha][col] = valor;
                    }
                }
                continue;
            }
            for (int linha = 0; linha < numLinhas; linha++) {
                if (index < dadosCifrados.length) {
                    matriz[linha][col] = dadosCifrados[index++];
                }
            }
        }

        byte[] dadosDecifrados = new byte[dadosCifrados.length];
        index = 0;
        for (int i = 0; i < numLinhas; i++) {
            for (int j = 0; j < numColunas; j++) {
                if (index < dadosCifrados.length && matriz[i][j] != 0) {
                    dadosDecifrados[index++] = matriz[i][j];
                }
            }
        }

        return dadosDecifrados;
    }

    private static byte[] DecifrarPorSubstituicao(String chave, byte[] dadosCifrados) {
        int tamanhoChaveAlinhada = dadosCifrados.length;
        StringBuilder chaveAlinhada = new StringBuilder(tamanhoChaveAlinhada);
    
        for (int i = 0; i < tamanhoChaveAlinhada; i++) {
            chaveAlinhada.append(chave.charAt(i % chave.length()));
        }
    
        byte[] chaveAlinhadaEmBytes = chaveAlinhada.toString().getBytes();
        byte[] dadosDecifrados = new byte[dadosCifrados.length];
    
        for (int i = 0; i < tamanhoChaveAlinhada; i++) {
            dadosDecifrados[i] = (byte)(dadosCifrados[i] - chaveAlinhadaEmBytes[i]);
        }
    
        return dadosDecifrados;
    }

    private static Integer[] obterOrdemColunas(String chave) {
        Integer[] ordem = new Integer[chave.length()];
        for (int i = 0; i < chave.length(); i++) {
            ordem[i] = i;
        }

        Arrays.sort(ordem, (a, b) -> Character.compare(chave.charAt(a), chave.charAt(b)));
        return ordem;
    }
}
