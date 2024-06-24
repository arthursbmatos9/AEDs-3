# ✨ Trabalho Prático 2 - Busca por palavras ✨

O presente trabalho implementa lista invertida para indexar palavras-chave dos títulos dos livros na classe ArquivoLivros, que gerencia a persistência de objetos da classe Livro em arquivos, bem como índices indiretos, árvore B+ e lista invertida para otimizar buscas.

# Resumo do funcionamento

**Inicialização**
Na inicialização,são criadas estruturas para gerenciar índices indiretos (indiceIndiretoISBN), relacionamento entre livros e categorias (relLivrosDaCategoria), e uma lista invertida (lista) para indexar palavras-chave dos títulos dos livros.

**Método Create**
Adiciona um novo livro ao arquivo de livros e atualiza os índices indiretos e a lista invertida com as palavras-chave do título do livro, removendo as "stop words". Na classe ArquivoLivro, o método "create" recebe um objeto do tipo Livro e insere na lista invertida cada palavra de seu título, além de adicionar no índice.

**Método Read**
Permite a pesquisa de livros por palavras de seu título. Para isso, utiliza-se a lista invertida para encontrar os livros correspondentes. Na classe ArquivoLivro, o método "readTitulo" recebe uma string contendo as palavras a serem buscadas e retorna um array de Livros que contém tais palavras (opção 5 no menu de livros). Neste método, o código retorna apenas os livros que contém todas as palavras que foram passadas. Também está presente o método "readISBN" para pesquisar um livro diretamente, que, apesar de não ser o propósito do trabalho, está presente (opção 2 no menu de livros).

**Método Delete**
Remove um livro do arquivo e atualiza os índices indiretos e a lista invertida correspondentes. Na classe ArquivoLivro, o método "delete" recebe o ID do livro a ser deletado e remove do índice e da lista invertida todas suas ocorrências. 

**Método Update**
Atualiza as informações de um livro, incluindo o ISBN, categoria, preço e título. Na classe ArquivoLivro, o método "update" recebe um livro e atualiza o índice e a lista invertida caso necessário.

**Métodos Auxiliares**
Há métodos auxiliares para manipulação de stop words, conversão de texto para minúsculas e remoção de acentos.

# Experiência do grupo

O desenvolvimento do trabalho prático foi desafiador para o nosso grupo. Conseguimos implementar todos os requisitos, mas enfrentamos dificuldades com o método read. A busca por título, utilizando índices indiretos e a lista invertida, foi complicada de otimizar. Foi necessário revisar várias vezes a lógica da busca para garantir sua eficiência e precisão. Apesar dos obstáculos, conseguimos superar esse desafio e alcançar os resultados desejados.

# Perguntas à serem respondidas

1 - A inclusão de um livro acrescenta os termos do seu título à lista invertida? 
    Sim! Ao incluir um livro, os termos do seu título são acrescentados à lista invertida.

2 - A alteração de um livro modifica a lista invertida removendo ou acrescentando termos do título?
    Sim, a alteração de um livro pode modificar a lista invertida removendo ou acrescentando termos do título conforme necessário.

3 - A remoção de um livro gera a remoção dos termos do seu título na lista invertida?
    Sim, a remoção de um livro gera a remoção dos termos do seu título na lista invertida.

4 - Há uma busca por palavras que retorna os livros que possuam essas palavras?
    Sim, há uma busca por palavras que retorna os livros que possuam essas palavras utilizando a lista invertida.

5 - Essa busca pode ser feita com mais de uma palavra?
    Sim, essa busca pode ser feita com mais de uma palavra, retornando os livros que possuem todas as palavras buscadas.

6 - As stop words foram removidas de todo o processo?
    Sim, as stop words foram removidas do processo de indexação na lista invertida, garantindo que apenas palavras relevantes sejam consideradas.

7 - Que modificação, se alguma, você fez para além dos requisitos mínimos desta tarefa?
    Nessa etapa, o grupo implementou apenas os requisitos minímos.

8 - O trabalho está funcionando corretamente?
    O trabalho está funcionando corretamente.

9 - O trabalho está completo?
    O trabalho está completo.

10 - O trabalho é original e não a cópia de um trabalho de um colega?
    Sim, o trabalho é original.
