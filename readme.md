Diego Priess, Guilherme Nathan Bertoldi

Organizamos o nosso programa de forma com que exista dois arquivos que podem ser executados,
o Criptografar e Descriptografar, o criptografar recebe a chave publica, e o caminho da arquivo de descriptografia e de criptografia,
após isso, criptografa o conteudo existente no arquivo descriptografia e coloca esse valor criptografado no arquivo de criptografia, 
já o de descriptografia, utiliza a chave privada para descriptografar o arquivo criptografado e cola no arquivo de criptografia.

Tivemos problemas com a parte de descriptografia, por algum motivo, estamos convertendo os bytes, para um código Base64 e não conseguimos transformar em String.

Aprendemos muito sobre criptografia rsa no java e o que julgamos que mais iremos utilizar no a dia, a manipulação de arquivos com java, criando e manipulado o conteudo dos arquivos.