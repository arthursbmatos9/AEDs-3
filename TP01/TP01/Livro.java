import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.NumberFormat;

import aeds3.Registro;

/**
 * Classe que representa um livro e implementa a interface Registro.
 */
public class Livro implements Registro {

    // Dados do livro
    private int ID; // Identificador do livro
    private String isbn; // ISBN do livro
    private String titulo; // Título do livro
    private float preco; // Preço do livro

    // Construtores
    /**
     * Construtor padrão que inicializa um livro com valores padrão.
     */
    public Livro() {
        this(-1, "", "", 0F);
    }

    /**
     * Construtor que inicializa um livro com ISBN, título e preço especificados.
     * 
     * @param isnb O ISBN do livro
     * @param titutlo O título do livro
     * @param preco O preço do livro
     */
    public Livro(String isbn, String titulo, float preco) {
        this(-1, isbn, titulo, preco);
    }

    /**
     * Construtor que inicializa um livro com ID, ISBN, título e preço especificados.
     * 
     * @param id O ID do livro
     * @param isbn O ISBN do livro
     * @param titulo O título do livro
     * @param preco O preço do livro
     */
    public Livro(int id, String isbn, String titulo, float preco) {
        this.ID = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.preco = preco;
    }

    // Getters e setters
    public int getID() {
        return this.ID;
    }

    public void setID(int i) {
        this.ID = i;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    /**
     * Converte o livro em um array de bytes.
     * 
     * @return O array de bytes representando o livro
     * @throws Exception Se ocorrer um erro durante a conversão
     */
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(ba_out);
        dos.writeInt(this.ID);
        dos.writeUTF(this.isbn);
        dos.writeUTF(this.titulo);
        dos.writeFloat(this.preco);
        return ba_out.toByteArray();
    }

    /**
     * Preenche os dados do livro a partir de um array de bytes.
     * 
     * @param ba O array de bytes representando o livro
     * @throws Exception Se ocorrer um erro durante a conversão
     */
    public void fromByteArray(byte[] ba) throws Exception {
        ByteArrayInputStream ba_in = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(ba_in);
        this.ID = dis.readInt();
        this.isbn = dis.readUTF();
        this.titulo = dis.readUTF();
        this.preco = dis.readFloat();
    }

    /**
     * Retorna uma representação em string do livro.
     * 
     * @return Uma string contendo os dados do livro formatados
     */
    public String toString() {
        return "ID: " + this.ID +
            "\nISBN: " + this.isbn +
            "\nTítulo: " + this.titulo +
            "\nPreço: " + NumberFormat.getCurrencyInstance().format(this.preco);
    }

    /**
     * Compara este livro com outro objeto.
     * 
     * @param b O objeto a ser comparado
     * @return Um valor negativo se este livro for menor, zero se forem iguais, ou um valor positivo se este livro for maior
     */
    public int compareTo(Object b) {
        return this.getID() - ((Livro) b).getID();
    }

    /**
     * Clona o livro.
     * 
     * @return Uma cópia do livro
     * @throws CloneNotSupportedException Se a clonagem não for suportada
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
