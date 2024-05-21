import java.io.*;
import aeds3.Arquivo;
import aeds3.Arvore;

/* LIVROS PARA TESTE 
 * 
 *  "9788563560278", "Odisseia", 15.99F);
    "9788584290482", "Ensino Híbrido", 39.90F
    "9786559790005", "Modernidade Líquida", 48.1F
    "9788582714911", "Memória", 55.58F
    "9786587150062", "Com Amor", 48.9F
    "9786587150062", "Ensina Híbidro", 16.99F
*/

/**
 * Classe principal que executa o programa de gerenciamento de livros.
 */
class Main {

  /**
   * Método principal que inicia a execução do programa.
   *
   * @param args Argumentos da linha de comando (não utilizados).
   * @throws Exception Exceção lançada em caso de erros durante a execução do
   *                   programa.
   */
  public static void main(String args[]) throws Exception {

    // Arvore que contêm os endereços e tamanhos dos registros marcados com lápide
    // no arquivo Livro.db
    Arvore arvore = new Arvore();
    new File("dados/Arvore.db").createNewFile();
    arvore.lerArvore();

    // Dados
    Arquivo<Livro> arqLivros;
    String isbn = "";
    String titulo = "";
    float preco = 0.0f;
    int opcao = -1;
    int contador = 0;

    // Arquivos da pasta dados
    new File("dados/livros.db").delete();
    new File("dados/livros.hash_d.db").delete();
    new File("dados/livros.hash_c.db").delete();

    // Exibição do menu
    System.out.println("----------------MENU-------------------");
    System.out.println("1) Inserir Livro");
    System.out.println("2) Atualizar Livro");
    System.out.println("3) Deletar Livro");
    System.out.println("4) Mostrar Livro");
    System.out.println("0) Sair");
    System.out.print("Digite a opcao: ");
    opcao = MyIO.readInt();

    try {
      arqLivros = new Arquivo<>("livros", Livro.class.getConstructor(), arvore);

      // Loop principal do programa
      do {
        switch (opcao) {
          case 1:
            do {
              // Leitura dos dados do livro a ser inserido
              System.out.print("ISBN: ");
              isbn = MyIO.readLine();
              System.out.print("Título: ");
              titulo = MyIO.readLine();
              System.out.print("Preço (usando vírgula): ");
              preco = MyIO.readFloat();

              // Verificação se algum campo está em branco
              if (isbn.isBlank() || titulo.isBlank() || preco == 0.0f) {
                System.out.println("Por favor, preencha todos os campos.");
              }
            } while (isbn.isBlank() || titulo.isBlank() || preco == 0.0f);

            // Criação do livro com os dados informados
            Livro livro = new Livro(-1, isbn, titulo, preco);
            int id = arqLivros.create(livro);
            System.out.println("Livro criado com o ID: " + id + "\n");

            contador++;
            break;

          case 2:
            int idLivro = -1;
            do {
              // Leitura do ID do livro e do novo título
              System.out.print("ID do livro a ser alterado: ");
              idLivro = MyIO.readInt();
              System.out.print("Novo Título: ");
              titulo = MyIO.readLine();
            } while (idLivro == -1 || titulo.isBlank());

            // Leitura do livro com o ID fornecido
            Livro livroLido = arqLivros.read(idLivro);

            // Atualização do título do livro
            livroLido.setTitulo(titulo);

            // Verificação se a atualização foi bem-sucedida
            if (arqLivros.update(livroLido))
              System.out.println("Livro de ID " + livroLido.getID() + " alterado!");
            else
              System.out.println("Livro de ID " + livroLido.getID() + " não encontrado!");
            break;

          case 3:
            idLivro = -1;
            do {
              // Leitura do ID do livro a ser excluído
              System.out.print("ID do livro a ser excluído: ");
              idLivro = MyIO.readInt();
            } while (idLivro == -1 || titulo.isBlank());

            // Leitura do livro com o ID fornecido
            livroLido = arqLivros.read(idLivro);

            // Verificação se a exclusão foi bem-sucedida
            if (arqLivros.delete(livroLido.getID()))
              System.out.println("Livro de ID " + livroLido.getID() + " excluído!");
            else
              System.out.println("Livro de ID " + livroLido.getID() + " não encontrado!");

          case 4:
            for (int i = 1; i <= contador; i++) {
              // Exibição dos livros cadastrados
              System.out.println("\nLivro:" + i + "\n" + arqLivros.read(i));
            }
            break;

          default:
            System.out.println("Opção Inválida");
            break;
        }

        // Leitura da próxima opção do menu
        System.out.print("\ndigite a opcao: ");
        opcao = MyIO.readInt();

      } while (opcao != 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
