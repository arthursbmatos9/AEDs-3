package aeds3;

import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;


/**
 * Classe responsável pela manipulação de arquivos.
 * @param <T> Tipo de registro a ser manipulado pelo arquivo.
 */
public class Arquivo<T extends Registro> {

  protected RandomAccessFile arquivo; // Arquivo de acesso aleatório
  protected String nomeEntidade = ""; // Nome da entidade associada ao arquivo
  protected Constructor<T> construtor; // Construtor da classe do registro
  final protected int TAM_CABECALHO = 4; // Tamanho do cabeçalho do arquivo
  protected HashExtensivel<ParIDEndereco> indiceDireto; // Índice direto do arquivo
  protected Arvore arvore; // Árvore de busca

  /**
   * Construtor da classe Arquivo.
   * @param na Nome da entidade associada ao arquivo.
   * @param c Construtor da classe do registro.
   * @param arvore Árvore de busca associada ao arquivo.
   * @throws Exception Se ocorrer um erro durante a inicialização do arquivo.
   */
  public Arquivo(String na, Constructor<T> c, Arvore arvore) throws Exception {
    this.nomeEntidade = na;
    this.construtor = c;
    this.arvore = arvore;
    arquivo = new RandomAccessFile("dados/" + na + ".db", "rw");
    if (arquivo.length() < TAM_CABECALHO) {
      arquivo.seek(0);
      arquivo.writeInt(0);
    }
    indiceDireto = new HashExtensivel<>(ParIDEndereco.class.getConstructor(),
        3,
        "dados/" + na + ".hash_d.db",
        "dados/" + na + ".hash_c.db");
  }

  /**
   * Cria um novo registro no arquivo.
   * @param obj Objeto a ser armazenado como registro.
   * @return O ID do registro criado.
   * @throws Exception Se ocorrer um erro durante a criação do registro.
   */
  public int create(T obj) throws Exception {
    arquivo.seek(0);
    int ultimoID = arquivo.readInt();
    ultimoID++;
    arquivo.seek(0);
    arquivo.writeInt(ultimoID);
    obj.setID(ultimoID);

    byte[] ba = obj.toByteArray();
    short tam = (short) ba.length;

    Long enderecoExistente = arvore.pesquisar(tam);

    if ( enderecoExistente != -1 ) {
      arquivo.seek(enderecoExistente);
      arvore.excluirChave(enderecoExistente);
    }
    else {
      arquivo.seek(arquivo.length());
    }

    long endereco = arquivo.getFilePointer();
    arquivo.writeByte(' '); //lapide
    arquivo.writeShort(tam);
    arquivo.write(ba);
    indiceDireto.create(new ParIDEndereco(obj.getID(), endereco));
    return obj.getID();
  }

  /**
   * Lê um registro do arquivo com base no ID.
   * @param id ID do registro a ser lido.
   * @return O registro lido, ou null se não encontrado.
   * @throws Exception Se ocorrer um erro durante a leitura do registro.
   */
  public T read(int id) throws Exception {
    T obj = construtor.newInstance();
    short tam;
    byte[] ba;

    ParIDEndereco pie = indiceDireto.read(id);
    long endereco = pie != null ? pie.getEndereco() : -1;
    if (endereco != -1) {
      arquivo.seek(endereco + 1); // pula o lápide também
      tam = arquivo.readShort();
      ba = new byte[tam];
      arquivo.read(ba);
      obj.fromByteArray(ba);
      return obj;
    }
    return null;
  }

  /**
   * Exclui um registro do arquivo com base no ID.
   * @param id ID do registro a ser excluído.
   * @return true se a exclusão for bem-sucedida, false caso contrário.
   * @throws Exception Se ocorrer um erro durante a exclusão do registro.
   */
  public boolean delete(int id) throws Exception {
    ParIDEndereco pie = indiceDireto.read(id);
    long endereco = pie != null ? pie.getEndereco() : -1;
    if (endereco != -1) {
      arquivo.seek(endereco);
      arquivo.writeByte('*');
      arquivo.seek(endereco + 1);
      short tamanho = arquivo.readShort();
      arvore.inserir(endereco, tamanho);
      indiceDireto.delete(id);
      return true;
    } else
      return false;
  }

  /**
   * Atualiza um registro no arquivo.
   * @param novoObj Novo objeto a ser atualizado.
   * @return true se a atualização for bem-sucedida, false caso contrário.
   * @throws Exception Se ocorrer um erro durante a atualização do registro.
   */
  public boolean update(T novoObj) throws Exception {
    T obj = construtor.newInstance();
    short tam, tam2;
    byte[] ba, ba2;
    ParIDEndereco pie = indiceDireto.read(novoObj.getID());
    long endereco = pie != null ? pie.getEndereco() : -1;

    if (endereco != -1) {
      arquivo.seek(endereco + 1); // pula o campo lápide
      tam = arquivo.readShort();
      ba = new byte[tam];
      arquivo.read(ba);
      obj.fromByteArray(ba);
      ba2 = novoObj.toByteArray();
      tam2 = (short) ba2.length;
      if (tam2 <= tam) {
        arquivo.seek(endereco + 1 + 2);
        arquivo.write(ba2);
      } else {
        arquivo.seek(endereco);
        arquivo.writeByte('*');
        arvore.inserir(endereco, tam);

        Long enderecoExistente = arvore.pesquisar(tam);

        if ( enderecoExistente != -1 ) {
          arquivo.seek(enderecoExistente);
        }
        else {
          arquivo.seek(arquivo.length());
        }
        
        long endereco2 = arquivo.getFilePointer();
        arquivo.writeByte(' ');
        arquivo.writeShort(tam2);
        arquivo.write(ba2);
        indiceDireto.update(new ParIDEndereco(novoObj.getID(), endereco2));
      }
      return true;
    }
    return false;
  }

  /**
   * Fecha o arquivo e o índice associado.
   * @throws Exception Se ocorrer um erro durante o fechamento dos recursos.
   */
  public void close() throws Exception {
    arquivo.close();
    indiceDireto.close();
  }
}
