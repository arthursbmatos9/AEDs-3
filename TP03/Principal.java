
import java.io.File;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import arquivos.ArquivoAutores;
import arquivos.ArquivoCategorias;
import arquivos.ArquivoLivros;
import entidades.Autor;
import entidades.Categoria;
import entidades.Livro;

public class Principal {

  private static Scanner console = new Scanner(System.in);
  public static RandomAccessFile arqLeitura, arqEscrita;
  public static LZW lzw = new LZW();

  public static void main(String[] args) {

    try {

      int opcao;
      do {
        System.out.println("\n\n\nBOOKAEDS 1.0");
        System.out.println("------------");
        System.out.println("\n> Início");
        System.out.println("\n1) Categorias");
        System.out.println("2) Autores");
        System.out.println("3) Livros");
        System.out.println("4) Verificar arquivos de Backup");
        System.out.println("\n9) Reiniciar BD");
        System.out.println("\n0) Sair");

        System.out.print("\nOpção: ");
        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }

        switch (opcao) {
          case 1:
            (new MenuCategorias()).menu();
            break;
          case 2:
            (new MenuAutores()).menu();
            break;
          case 3:
            (new MenuLivros()).menu();
            break;
          case 4:
            verificarBackup();
          case 9:
            preencherDados();
            break;
          case 0:
            break;
          default:
            System.out.println("Opção inválida");
        }
      } while (opcao != 0);

      /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

      LocalDateTime diaHora = LocalDateTime.now(); // pega dia e horario
      DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd_MM_yyyy___HH_mm_ss");// define o formato
      String diaHoraFormatado = diaHora.format(formato);// formata a data e hora atual

      File pasta = new File("backup/" + diaHoraFormatado); // cria a pasta BACKUP com data e horario
      pasta.mkdir();

      // cria cada arquivo codificado na pasta de backup que foi criada
      criarArquivo("autores.db", diaHoraFormatado);
      criarArquivo("autores.hash_c.db", diaHoraFormatado);
      criarArquivo("autores.hash_d.db", diaHoraFormatado);
      criarArquivo("blocos.listainv.db", diaHoraFormatado);
      criarArquivo("categorias.db", diaHoraFormatado);
      criarArquivo("categorias.hash_c.db", diaHoraFormatado);
      criarArquivo("categorias.hash_d.db", diaHoraFormatado);
      criarArquivo("dicionario.listainv.db", diaHoraFormatado);
      criarArquivo("livros_categorias.btree.db", diaHoraFormatado);
      criarArquivo("livros_isbn.hash_c.db", diaHoraFormatado);
      criarArquivo("livros_isbn.hash_d.db", diaHoraFormatado);
      criarArquivo("livros.db", diaHoraFormatado);
      criarArquivo("livros.hash_c.db", diaHoraFormatado);
      criarArquivo("livros.hash_d.db", diaHoraFormatado);

      arqLeitura.close();
      arqEscrita.close();

    }

    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void criarArquivo(String nomeArquivo, String diaHoraFormatado) throws Exception {
    byte[] msgOriginal, codificado;

    arqLeitura = new RandomAccessFile("dados/" + nomeArquivo, "r"); // abre o arquivo para leitura
    msgOriginal = new byte[(int) arqLeitura.length()];
    arqLeitura.readFully(msgOriginal); // salva o arquivo com um ARRAY DE BYTES
    codificado = lzw.codificarArquivo(msgOriginal); // codifica o arquivo
    arqEscrita = new RandomAccessFile("backup/" + diaHoraFormatado + "/" + nomeArquivo, "rw"); // arquivo para escrita
    arqEscrita.write(codificado); // escrevendo o arquivo codificado
  }

  public static void verificarBackup() throws Exception {

    File diretorio = new File("backup");
    File[] opcoesBackup = diretorio.listFiles();
    int contador = 0;
    System.out.println("\nQual pasta de Backup deseja acessar? Digite o numero correspondente");

    for (File f : opcoesBackup) {
      System.out.println(contador + "- " + f);
      contador++;
    }
    int versaoBackup = console.nextInt();
    String nomeVersao = opcoesBackup[versaoBackup].getName();

    diretorio = new File("dados");
    opcoesBackup = diretorio.listFiles();
    contador = 0;
    System.out.println("\nQual arquivo deseja acessar? Digite o numero correspondente");

    for (File f : opcoesBackup) {
      System.out.println(contador + "- " + f);
      contador++;
    }
    int arquivoBackup = console.nextInt();
    String nomeArquivo = opcoesBackup[arquivoBackup].getName();

    RandomAccessFile arquivoRecuperado = new RandomAccessFile("backup/" + nomeVersao + "/" + nomeArquivo, "r");
    byte[] arqCodificado = new byte[(int) arquivoRecuperado.length()];
    arquivoRecuperado.readFully(arqCodificado);
    byte[] arqDecodificado = LZW.decodifica(arqCodificado);

    String msgDecodificada = new String(arqDecodificado);
    System.out.println(msgDecodificada);
    arquivoRecuperado.close();
    /*
     * System.out.println("0- autores.db");
     * System.out.println("1- autores.hash_c.db");
     * System.out.println("2- autores.hash_d.db");
     * System.out.println("3- blocos.listainv.db");
     * System.out.println("4- categorias.db");
     * System.out.println("5- categorias.hash_c.db");
     * System.out.println("6- categorias.hash_d.db");
     * System.out.println("7- dicionario.listainv.db");
     * System.out.println("8- livros_categorias.btree.db");
     * System.out.println("9- livros_isbn.hash_c.db");
     * System.out.println("10- livros_isbn.hash_d.db");
     * System.out.println("11- livros.db");
     * System.out.println("12- livros.hash_c.db");
     * System.out.println("13- livros.hash_d.db");
     */

  }

  public static void preencherDados() {
    try {
      new File("dados/categorias.db").delete();
      new File("dados/categorias.hash_d.db").delete();
      new File("dados/categorias.hash_c.db").delete();
      new File("dados/autores.db").delete();
      new File("dados/autores.hash_d.db").delete();
      new File("dados/autores.hash_c.db").delete();
      new File("dados/livros.db").delete();
      new File("dados/livros.hash_d.db").delete();
      new File("dados/livros.hash_c.db").delete();
      new File("dados/livros_isbn.hash_d.db").delete();
      new File("dados/livros_isbn.hash_c.db").delete();
      new File("dados/livros_categorias.btree.db").delete();
      new File("dados/dicionario.listainv.db").delete();
      new File("dados/blocos.listainv.db").delete();

      ArquivoLivros arqLivros = new ArquivoLivros();
      ArquivoCategorias arqCategorias = new ArquivoCategorias();
      ArquivoAutores arqAutores = new ArquivoAutores();

      arqCategorias.create(new Categoria("Romance"));
      arqCategorias.create(new Categoria("Educação"));
      arqCategorias.create(new Categoria("Sociologia"));
      arqCategorias.create(new Categoria("Policial"));
      arqCategorias.create(new Categoria("Aventura"));
      arqCategorias.create(new Categoria("Suspense"));

      arqAutores.create(new Autor("Homero"));
      arqAutores.create(new Autor("Lilian Bacich"));
      arqAutores.create(new Autor("Adolfo Tanzi Neto"));
      arqAutores.create(new Autor("Zygmunt Bauman"));
      arqAutores.create(new Autor("Plínio Dentzien"));
      arqAutores.create(new Autor("Ivan Izquierdo"));
      arqAutores.create(new Autor("Mariana Zapata"));

      arqLivros.create(new Livro("9788563560278", "Odisseia", 15.99F, 1));
      arqLivros.create(new Livro("9788584290483", "Ensino Híbrido", 39.90F, 2));
      arqLivros.create(new Livro("9786559790005", "Modernidade Líquida", 48.1F, 3));
      arqLivros.create(new Livro("9788582714911", "Memória", 55.58F, 1));
      arqLivros.create(new Livro("9786587150062", "Com Amor", 48.9F, 1));

      arqLivros.close();
      arqCategorias.close();
      arqAutores.close();

      System.out.println("Banco de dados reinicializado com dados de exemplo.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
