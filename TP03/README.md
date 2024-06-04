# ✨ Trabalho Prático 3 - Backup Compactado ✨

O presente trabalho implementa uma rotina de backup sobre arquivos de dados. Tal backup utiliza a codificação LZW, que comprime os arquivos para que eles possam ocupar menos espaço. Existe também uma maneira de visualizar tais dados de forma original, por meio da função de decodificar o arquivo compactado.

# Resumo do funcionamento

Ao iniciar o código, o usuário é direcionado ao menu principal, onde existem opções de realizar alterações nas entidades ou de visualizar um arquivo de backup. Com o propósito de aplicar a compactação e descompactação usando o algoritmo LZW, todos os arquivos são compactados quando o usuário clica na opção de "Sair" no menu, tendo em mente que realizou todas as mudanças necessárias.

Então, usamos a classe "LocalDateTime" que pega a data e horário atual para ser o nome da pasta de backup, como exemplo: 25_05_2024___17_53_40. Assim, o código chama a função "criarArquivo" passando como parâmetro o nome do arquivo e o dia com o horário. Em tal função, o código lê o arquivo original e transforma-o em um vetor de bytes, para que possa ser compactado usando o código LZW. Essa sequência de operações acontece para todos os arquivos de dados.

Para descompactar os arquivos que estão nas pastas de backup, o usuário seleciona a opção "Verificar arquivos de Backup" no menu principal, para ser direcionado à função "verificarBackup" que mostra todas as pastas de backup disponíveis, com sua data e hora. Após escolher, é necessário fazer a escolha do arquivo que deseja visualizar, digitando seu número correspondente. Tendo a versão e o arquivo em mãos, o código consegue acessar o arquivo desejado, que está compactado, transforma-o para um vetor de bytes e então consegue chamar a função de decodificar do LZW.

# Experiência do grupo

O desenvolvimento do terceiro trabalho prático transcorreu de forma bastante tranquila. O grupo organizou-se e planejou bem os requisitos que eram necessários, o que resultou em um resultado satisfatório ao final do projeto, com todos os requisitos atendidos conforme o esperado.

# Perguntas a serem respondidas

1 - Há uma rotina de compactação usando o algoritmo LZW para fazer backup dos arquivos?
    
    Sim. Todos os arquivos são compactados e salvos em uma nova pasta de backup quando o usuário seleciona a opção de sair, ou seja, após fazer todas as atualizações necessárias no momento.

2 - Há uma rotina de descompactação usando o algoritmo LZW para recuperação dos arquivos?

    Sim. Tal rotina acontece após o usuário escolher a versão e arquivo que deseja visualizar.
    
3 - O usuário pode escolher a versão a recuperar?

    Sim. Logo no menu principal existe a opção do usuário escolher qual a versão e arquivo que deseja descompactar para visualizar. 
    
4 - Qual foi a taxa de compressão alcançada por esse backup? (Compare o tamanho dos arquivos compactados com os arquivos originais)

    Cada arquivo teve uma taxa de compressão diferente. Nos piores casos, apenas de 10%, mas chegando em até 80% de compressão. Em média, percebemos que tal taxa foi de aproximadamente 50%.
    
5 - O trabalho está funcionando corretamente?

    Sim, as rotinas de compactactação, descompactação e formação das pastas de backup estão funcionando corretamente.
    
6 - O trabalho está completo?

    Sim, o trabalho está completo.
    
7 - O trabalho é original e não a cópia de um trabalho de um colega?

    Sim, o trabalho é original.
