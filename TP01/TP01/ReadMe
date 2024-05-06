Durante o desenvolvimento do TP1, pudemos implementar mudanças no código para que ele controle os espaços vazios de um arquivo, gerados quando um registro é excluído. Reaproveitamos os espaços marcados como excluídos para inserir novos registros, para economizar memória, quando possível. Desenvolver o trabalho prático foi uma excelente oportunidade de aplicar os conhecimentos vistos até o momento em AEDs3. Tivemos certas dificuldades na hora de decidir qual seria a melhor forma de armazenar os endereços dos registros excluídos, se seria algum tipo de árvore, um array, um hash... Optamos pela árvore binária, em que armazenamos o par endereço e tamanho, para podermos caminhar para a direita ou esquerda dependendo do tamanho desejado. Contudo, conseguimos implementar todos os requisitos e os resultados atenderam às expectativas do grupo!

O que você considerou como perda aceitável para o reuso de espaços vazios, isto é, quais são os critérios para a gestão dos espaços vazios? O reaproveitamento é realizado em apenas espaços que tem entre 90% e 100% do tamanho do registro que vai ser inserido.

O código do CRUD com arquivos de tipos genéricos está funcionando corretamente? Sim, está funcionando corretamente.

O CRUD tem um índice direto implementado com a tabela hash extensível? Sim.

A operação de inclusão busca o espaço vazio mais adequado para o novo registro antes de acrescentá-lo ao fim do arquivo? Sim. Na hora de inserir um novo registro, o programa busca entre os espaços em branco aquele que tenha tamanho mais próximo ao tamanho do registro que desejamos inserir (no mínimo 90%), antes de inserí-lo no fim do arquivo.

A operação de alteração busca o espaço vazio mais adequado para o registro quando ele cresce de tamanho antes de acrescentá-lo ao fim do arquivo? Sim. Na operação de atualização de um registro que cresceu de tamanho, o programa procura entre os espaços em branco algum que tenha tamanho mais próximo ao tamanho do registro que foi atualizado (90%), antes de inserí-lo no fim do arquivo.

As operações de alteração (quando for o caso) e de exclusão estão gerenciando os espaços vazios para que possam ser reaproveitados? Sim. A inclusão e a atualização gerenciam os espaços vazios

O trabalho está funcionando corretamente? Sim. O trabalho está com todas as operações do CRUD funcionando.

O trabalho está completo? Sim. O trabalho está completo, contemplando o reuso dos espaços em branco tanto na operação de inserção quanto na operação de atualização de um registro no arquivo.

O trabalho é original e não a cópia de um trabalho de um colega? O trabalho foi produzido de forma 100% autoral pelos membros do grupo.
