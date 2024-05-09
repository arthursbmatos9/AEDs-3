package arquivos;
import java.util.*;
import java.io.*;
import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.HashExtensivel;
import aeds3.ParIntInt;
import entidades.Livro;
import aeds3.ListaInvertida;
import java.text.Normalizer;

public class ArquivoLivros extends Arquivo<Livro> {

  HashExtensivel<ParIsbnId> indiceIndiretoISBN;
  ArvoreBMais<ParIntInt> relLivrosDaCategoria;
  ListaInvertida lista;
  RandomAccessFile sw = new RandomAccessFile("dados/SW.txt", "r");
  ArrayList<String> stopWords = new ArrayList<>();
  

  public ArquivoLivros() throws Exception {

    super("livros", Livro.class.getConstructor());
    indiceIndiretoISBN = new HashExtensivel<>(
        ParIsbnId.class.getConstructor(),
        4,
        "dados/livros_isbn.hash_d.db",
        "dados/livros_isbn.hash_c.db");
    relLivrosDaCategoria = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "dados/livros_categorias.btree.db");
    lista= new ListaInvertida(4, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");//instanciaçao da lista invertida
    String linhaArq;
    while ((linhaArq = sw.readLine()) != null) {
      stopWords.add(linhaArq);
    }
   // lista.print();
  }


  // ---------------------
  // STOPWORDS
  // ---------------------

  public static String minusculo_RemoverAcentos(String s) {
    s = s.toLowerCase();
    // metodo encontrado na internet para remover acentos
    return Normalizer.normalize(s, Normalizer.Form.NFD)
        .replaceAll("[^\\p{ASCII}]", "");
  }

  public static String[] stopWords(ArrayList<String> stopWords, String[] chaves) throws Exception {
    // armazena as palavras que nao sao stop words
    ArrayList<String> chaves2 = new ArrayList<>();

    // itera sobre as stopwords para ver se tem alguma
    for (String c : chaves) {
    if (stopWords.contains(c) == false) {
        chaves2.add(c);
    }
    }
    // joga as palavras do arraylist em um array normal para retornar
    String[] resp = new String[chaves2.size()];
    for (int i = 0; i < chaves2.size(); i++) {
    resp[i] = chaves2.get(i);
    }

    return resp;
  }
  /*---------------------------------------------------------------- */

  
   // ---------------------
  // CREATE
  // ---------------------
  @Override
  public int create(Livro obj) throws Exception {

    int id = super.create(obj);
    obj.setID(id);
    indiceIndiretoISBN.create(new ParIsbnId(obj.getIsbn(), obj.getID()));
    relLivrosDaCategoria.create(new ParIntInt(obj.getIdCategoria(), obj.getID()));

    //lidando com as sw
    String chave= obj.getTitulo();
    chave = minusculo_RemoverAcentos(chave);

    // identifica as stop words e remove-as
    String[] chaves = chave.split(" ");
    chaves = stopWords(stopWords, chaves);

    // cria um novo registro para cada palavra da chave
    for (int i = 0; i < chaves.length; i++) {
      lista.create(chaves[i], id);
    }
    
    return id;

  }

   // ---------------------
  // READ
  // ---------------------
  public Livro readISBN(String isbn) throws Exception {
    ParIsbnId pii = indiceIndiretoISBN.read(ParIsbnId.hashIsbn(isbn));
    if (pii == null)
      return null;
    int id = pii.getId();
    return super.read(id);
  }


  public Livro[] readTitulo(String titulo) throws Exception {
    
      //tratamento da string
      titulo = minusculo_RemoverAcentos(titulo);
      String[] palavrasChaves = titulo.split(" ");
      palavrasChaves = stopWords(stopWords, palavrasChaves);
  
      Set<Integer> indices = new HashSet<>();//armazenar os indices 
  
      for (String palavra : palavrasChaves) {
          int[] indicesPalavra = lista.read(palavra);
          if (indices.isEmpty()) {
              for (int indice : indicesPalavra) {
                  indices.add(indice);
              }
          } else {
              Set<Integer> indicesPalavraSet = new HashSet<>();
              for (int indice : indicesPalavra) {
                  indicesPalavraSet.add(indice);
              }
              indices.retainAll(indicesPalavraSet);
          }
      }
  
      if (!indices.isEmpty()) {
          Livro[] resposta = new Livro[indices.size()];
          int i = 0;
          for (int indice : indices) {
              resposta[i++] = super.read(indice);
          }
          return resposta;
      } else {
          return null;
      }
  }


  @Override

 // ---------------------
  // DELETE
  // ---------------------
  public boolean delete(int id) throws Exception {
    Livro obj = super.read(id);

    if (obj != null)//se o livro foi encontrado 

      if (indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(obj.getIsbn()))&& relLivrosDaCategoria.delete(new ParIntInt(obj.getIdCategoria(), obj.getID()))){//exclui do indicie indireto

        //tratamento da string
        String chave = obj.getTitulo();
        chave = minusculo_RemoverAcentos(chave);
        String[] chaves = chave.split(" ");
        chaves = stopWords(stopWords, chaves);

        //deleta da lista os indices
        for (int i = 0; i < chaves.length; i++) {
          lista.delete(chaves[i], id);
        }
        return super.delete(id);
      }
    return false;
  }

  @Override

   // ---------------------
  // UPDATE
  // ---------------------
  public boolean update(Livro novo) throws Exception {
    Livro livroAntigo = super.read(novo.getID());
    
  
    if (livroAntigo != null) {//Testa se existe um livro para ser alterado com o id 

      //variaveis referentes aos novos atributos
      String isbn_novo = novo.getIsbn();
      String novo_titulo =novo.getTitulo();
      int nova_categoria=novo.getIdCategoria();
      float novo_preco=novo.getPreco();

      // Testa se o antigo isbn é igual ao novo (se foi alterado)
      if (livroAntigo.getIsbn().compareTo(isbn_novo) != 0) {
        
        indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(livroAntigo.getIsbn()));// deleta o antigo do indicie indireto 
        indiceIndiretoISBN.create(new ParIsbnId(isbn_novo, novo.getID()));//cria um novo indicie indireto para o novo livro
      }

      // Testa se a antiga categoria é igual a nova (se foi alterado)
      if (livroAntigo.getIdCategoria() != nova_categoria) {
        relLivrosDaCategoria.delete(new ParIntInt(livroAntigo.getIdCategoria(), livroAntigo.getID()));
        relLivrosDaCategoria.create(new ParIntInt(nova_categoria, novo.getID()));
      }

      // Testa se o antigo preço é igual ao  novo (se foi alterado)
      if (livroAntigo.getPreco() != novo_preco) {
        novo.setPreco(novo_preco);
      }

      // Testa se o antigo titulo é igual ao  novo (se foi alterado)
      if(livroAntigo.getTitulo() != novo_titulo) {
        //tratatmento da string
        String chave;
        chave = minusculo_RemoverAcentos(livroAntigo.getTitulo());
        String[] chaves = chave.split(" ");
        chaves = stopWords(stopWords, chaves);

        //exclusao dos indices antigos
        for(int i = 0; i < chaves.length; i++)
        {
          lista.delete(chaves[i], livroAntigo.getID()); 
        }

        String chave2 = minusculo_RemoverAcentos(novo_titulo);
        String[] chaves2 = chave2.split(" ");
        chaves2 = stopWords(stopWords, chaves);
        
        //criaçao dos indicies novos
        for(int i = 0; i < chaves2.length; i++)
        {
          lista.create(chaves2[i], novo.getID());
        }
      }

      //sucesso
      return super.update(novo);
    }
    //nao havia um livro para ser alterado com esse isbn
    return false;
  }

    
}
