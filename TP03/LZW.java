import java.io.*;
import java.util.ArrayList;

public class LZW {

    public static final int BITS_POR_INDICE = 12;

    public byte[] codificarArquivo(byte[] msgOriginal) {

        try {
            // RandomAccessFile arq = new RandomAccessFile("dados/housing.xlsx", "r");

            // byte[] msgOriginal = new byte[(int) arq.length()];
            // arq.readFully(msgOriginal);

            // String nome = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";
            // byte[] msgOriginal = nome.getBytes();

            byte[] msgCodificada = codifica(msgOriginal);

            System.out.println("mensagem original tem " + msgOriginal.length + " bytes");
            System.out.println("codificado em " + msgCodificada.length + " bytes");

            // byte[] msgDecodificada = decodifica(msgCodificada);
            // System.out.println(new String(msgDecodificada));

            // arquivoBackup.write(msgCodificada);

            return msgCodificada;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] codifica(byte[] msgOriginal) throws Exception {

        ArrayList<ArrayList<Byte>> dicionario = new ArrayList<>(); // dicionario
        ArrayList<Byte> vetorBytes; // auxiliar para cada elemento do dicionario
        ArrayList<Integer> saida = new ArrayList<>();

        // inicializa o dicionário
        byte b;
        for (int j = -128; j < 128; j++) {
            b = (byte) j;
            vetorBytes = new ArrayList<>();
            vetorBytes.add(b);
            dicionario.add(vetorBytes);
        }

        int i = 0;
        int indice = -1;
        int ultimoIndice;
        while (indice == -1 && i < msgOriginal.length) { // testa se o último vetor de bytes não parou no meio caminho
                                                         // por
                                                         // falta de bytes
            vetorBytes = new ArrayList<>();
            b = msgOriginal[i];
            vetorBytes.add(b);
            indice = dicionario.indexOf(vetorBytes);
            ultimoIndice = indice;

            while (indice != -1 && i < msgOriginal.length - 1) {
                i++;
                b = msgOriginal[i];
                vetorBytes.add(b);
                ultimoIndice = indice;
                indice = dicionario.indexOf(vetorBytes);

            }

            // acrescenta o último índice à saída
            saida.add(ultimoIndice);

            // acrescenta o novo vetor de bytes ao dicionário
            if (dicionario.size() < (Math.pow(2, BITS_POR_INDICE))) {
                dicionario.add(vetorBytes);
            }

        }

        // System.out.println("Indices");
        // System.out.println(saida);
        System.out.println("Dicionário tem " + dicionario.size() + " elementos");

        BitSequence bs = new BitSequence(BITS_POR_INDICE);
        for (i = 0; i < saida.size(); i++) {
            bs.add(saida.get(i));
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(bs.size());
        dos.write(bs.getBytes());
        return baos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static byte[] decodifica(byte[] msgCodificada) throws Exception {

        ByteArrayInputStream bais = new ByteArrayInputStream(msgCodificada);
        DataInputStream dis = new DataInputStream(bais);
        int n = dis.readInt();
        byte[] bytes = new byte[msgCodificada.length - 4];
        dis.read(bytes);
        BitSequence bs = new BitSequence(BITS_POR_INDICE);
        bs.setBytes(n, bytes);

        // Recupera os números do bitset
        ArrayList<Integer> entrada = new ArrayList<>();
        int i, j;
        for (i = 0; i < bs.size(); i++) {
            j = bs.get(i);
            entrada.add(j);
        }

        // inicializa o dicionário
        ArrayList<ArrayList<Byte>> dicionario = new ArrayList<>(); // dicionario
        ArrayList<Byte> vetorBytes; // auxiliar para cada elemento do dicionario
        byte b;
        for (j = -128; j < 128; j++) {
            b = (byte) j;
            vetorBytes = new ArrayList<>();
            vetorBytes.add(b);
            dicionario.add(vetorBytes);
        }

        // Decodifica os números
        ArrayList<Byte> proximoVetorBytes;
        ArrayList<Byte> msgDecodificada = new ArrayList<>();
        i = 0;
        while (i < entrada.size()) {

            // decodifica o número
            vetorBytes = (ArrayList<Byte>) (dicionario.get(entrada.get(i)).clone());
            msgDecodificada.addAll(vetorBytes);

            // decodifica o próximo número
            i++;
            if (i < entrada.size()) {
                proximoVetorBytes = dicionario.get(entrada.get(i));
                vetorBytes.add(proximoVetorBytes.get(0));

                // adiciona o vetor de bytes (+1 byte do próximo vetor) ao fim do dicionário
                if (dicionario.size() < Math.pow(2, BITS_POR_INDICE))
                    dicionario.add(vetorBytes);
            }

        }

        byte[] msgDecodificadaBytes = new byte[msgDecodificada.size()];
        for (i = 0; i < msgDecodificada.size(); i++)
            msgDecodificadaBytes[i] = msgDecodificada.get(i);
        return msgDecodificadaBytes;

    }
}