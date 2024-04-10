
import java.io.*;

import aeds3.Arquivo;

class Principal {

  public static void main(String args[]) {

    new File("dados/livros.db").delete();
    new File("dados/livros.hash_d.db").delete();
    new File("dados/livros.hash_c.db").delete();

    Arquivo<Livro> arqLivros;
    // Livro l1 = new Livro(-1, "111111111111", "Braz Matos", 99.99F);
    // Livro l2 = new Livro(-1, "9788584290483", "Ensino Híbrido", 39.90F);
    // Livro l3 = new Livro(-1, "9787559790007", "Modernidade Líquida", 48.1F);
    // Livro l4 = new Livro(-1, "9788582714912", "Memória", 55.58F);
    // Livro l5 = new Livro(-1, "9787587150073", "Com Amor", 48.9F);
    Livro l6 = new Livro(-1, "9787587150073", "Arthur de Sa Matos", 180.5F);
    int id1, id2, id3, id4, id6;

    try {
      arqLivros = new Arquivo<>("livros", Livro.class.getConstructor());

      // id6 = arqLivros.create(l6);
      // System.out.println("Livro criado com o ID: " + id6);

      // l7.setTitulo("Arthur Braz Matos");
      // if (arqLivros.update(l7))
      // System.out.println("Livro de ID " + l7.getID() + " alterado!");
      // else
      // System.out.println("Livro de ID " + l7.getID() + " não encontrado!");

      if (arqLivros.delete(3))
        System.out.println("Livro de ID " + 3 + " excluído!");
      else
        System.out.println("Livro de ID " + 3 + " não encontrado!");

      // arqLivros.reorganizar();

      // System.out.println("\nLivro:\n" + arqLivros.read(3));
      // System.out.println("\nLivro 1:\n" + arqLivros.read(1));
      // System.out.println("\nLivro 5:\n" + arqLivros.read(5));
      // System.out.println("\nLivro 4:\n" + arqLivros.read(4));
      // System.out.println("\nLivro 2:\n" + arqLivros.read(2));

      arqLivros.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}