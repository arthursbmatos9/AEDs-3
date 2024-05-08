package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.RandomAccessFile;

/**
 * Classe que define um nó de uma árvore binária.
 */
class No {

    // Dados do nó
    protected Long endereco;
    protected short tamanho;
    protected No esq, dir; // Referências para os nós esquerdo e direito

    /**
     * Construtor padrão que inicializa um nó com valores padrão para o endereço e
     * tamanho.
     */
    public No() {
        this((long) -1, (short) -1);
    }

    /**
     * Construtor que inicializa um nó com um endereço e tamanho específicos.
     * 
     * @param endereco O endereço no nó
     * @param tamanho  O tamanho no nó
     */
    public No(Long endereco, short tamanho) {
        this.endereco = endereco;
        this.tamanho = tamanho;
        esq = dir = null;
    }
}

/**
 * Classe que implementa uma árvore binária de busca.
 */
public class Arvore {
    // Dados da árvore
    public No raiz; // Raiz da árvore
    protected RandomAccessFile arquivoArvore; // Arquivo para armazenar a árvore

    /**
     * Construtor que inicializa a árvore e abre um arquivo para leitura e escrita.
     * 
     * @throws Exception Se ocorrer um erro na abertura do arquivo
     */
    public Arvore() throws Exception {
        raiz = null;

        // Abertura leitura dos dados do arquivo
        arquivoArvore = new RandomAccessFile("dados/" + "Arvore" + ".db", "rw");
    }

    /**
     * Insere um novo nó na árvore.
     * 
     * @param endereco O endereço do nó a ser inserido
     * @param tamanho  O tamanho do nó a ser inserido
     * @throws Exception Se ocorrer um erro durante a inserção
     */
    public void inserir(Long endereco, short tamanho) throws Exception {
        raiz = inserir(endereco, tamanho, raiz);
    }

    /**
     * Método auxiliar para inserção de um novo nó na árvore.
     * 
     * @param endereco O endereço do nó a ser inserido
     * @param tamanho  O tamanho do nó a ser inserido
     * @param i        O nó atual sendo considerado na inserção
     * @return O nó atualizado após a inserção
     * @throws Exception Se ocorrer um erro durante a inserção
     */
    public No inserir(Long endereco, short tamanho, No i) throws Exception {
        if (i == null) {
            i = new No(endereco, tamanho);
        } else if (tamanho < i.tamanho) {
            i.esq = inserir(endereco, tamanho, i.esq);
        } else if (tamanho > i.tamanho) {
            i.dir = inserir(endereco, tamanho, i.dir);
        } else {
            throw new Exception("Erro ao inserir.");
        }
        return i;
    }

    /**
     * Pesquisa o endereço de um nó na árvore com base em um tamanho fornecido.
     * 
     * @param tamanho O tamanho do nó a ser pesquisado
     * @return O endereço do nó encontrado ou -1 se não encontrado
     */
    public long pesquisar(short tamanho) {
        return pesquisar(tamanho, raiz);
    }

    /**
     * Método auxiliar para pesquisa de um nó na árvore com base em um tamanho
     * fornecido.
     * 
     * @param tamanho O tamanho do nó a ser pesquisado
     * @param i       O nó atual sendo considerado na pesquisa
     * @return O endereço do nó encontrado ou -1 se não encontrado
     */
    public long pesquisar(short tamanho, No i) {
        if (i == null) {
            return (long) -1;
        } else if (tamanho >= 0.9 * i.tamanho && tamanho <= 1.0 * i.tamanho) {
            return i.endereco;
        } else if (tamanho < i.tamanho) {
            pesquisar(tamanho, i.esq);
        } else if (tamanho > i.tamanho) {
            pesquisar(tamanho, i.dir);
        }
        return (long) -1;
    }

    /**
     * Exclui um nó da árvore com base em um endereço fornecido.
     * 
     * @param chave O endereço do nó a ser excluído
     */
    public void excluirChave(Long chave) {
        raiz = deletar(raiz, chave);
    }

    /**
     * Método auxiliar para exclusão de um nó da árvore com base em um endereço
     * fornecido.
     * 
     * @param raiz     A raiz da subárvore atual sendo considerada
     * @param endereco O endereço do nó a ser excluído
     * @return A raiz atualizada após a exclusão
     */
    public No deletar(No raiz, Long endereco) {
        if (raiz == null)
            return raiz;

        if (endereco < raiz.endereco)
            raiz.esq = deletar(raiz.esq, endereco);
        else if (endereco > raiz.endereco)
            raiz.dir = deletar(raiz.dir, endereco);
        else {
            if (raiz.esq == null)
                return raiz.dir;
            else if (raiz.dir == null)
                return raiz.esq;

            raiz.endereco = menorValor(raiz.dir);

            raiz.dir = deletar(raiz.dir, raiz.endereco);
        }

        return raiz;
    }

    /**
     * Encontra o menor valor em uma subárvore.
     * 
     * @param raiz A raiz da subárvore
     * @return O menor valor encontrado
     */
    public long menorValor(No raiz) {
        long menor = raiz.endereco;
        while (raiz.esq != null) {
            menor = raiz.esq.endereco;
            raiz = raiz.esq;
        }
        return menor;
    }

    /**
     * Lê a árvore a partir de um arquivo.
     * 
     * @throws Exception Se ocorrer um erro durante a leitura
     */
    public void lerArvore() throws Exception {
        arquivoArvore.seek(0);
        byte[] ba = new byte[(short) arquivoArvore.length()];
        arquivoArvore.read(ba);
        ByteArrayInputStream ba_in = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(ba_in);
        lerArvore(raiz, dis);
    }

    /**
     * Método auxiliar para leitura da árvore a partir de um arquivo.
     * 
     * @param i   O nó atual sendo considerado na leitura
     * @param dis O fluxo de entrada de dados
     * @throws Exception Se ocorrer um erro durante a leitura
     */
    public void lerArvore(No i, DataInputStream dis) throws Exception {
        if (i != null) {
            lerArvore(i.esq, dis);
            long endereco = dis.readLong();
            short tam = dis.readShort();
            inserir(endereco, tam);
            lerArvore(i.dir, dis);
        }
    }

    /**
     * Salva a árvore em um arquivo.
     * 
     * @throws Exception Se ocorrer um erro durante a escrita
     */
    public void salvarArvore() throws Exception {
        arquivoArvore.setLength(0);
        ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(ba_out);
        salvarArvore(raiz, dos);
        arquivoArvore.write(ba_out.toByteArray());
    }

    /**
     * Método auxiliar para salvar a árvore em um arquivo.
     * 
     * @param i   O nó atual sendo considerado na escrita
     * @param dos O fluxo de saída de dados
     * @throws Exception Se ocorrer um erro durante a escrita
     */
    public void salvarArvore(No i, DataOutputStream dos) throws Exception {
        if (i != null) {
            salvarArvore(i.esq, dos);
            long endereco = i.endereco;
            short tam = i.tamanho;

            dos.writeLong(endereco);
            dos.writeShort(tam);

            salvarArvore(i.dir, dos);
        }
    }
}
