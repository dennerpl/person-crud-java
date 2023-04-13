# Person CRUD
API para gerenciamento pessoas e seus respectivos endereços.

# Tecnologias Utilizadas
- Java
- Spring Boot
- Maven 
- Banco de dados H2
- JUnit

## Acesso ao banco de dados

- Os dados estão sendo salvos no arquivo database na pasta data
- Após iniciar o projeto poderá acessar o banco de dados para ver as informações salvas lá e execultar consultas. Utilize o endpoint: http://localhost:8080/h2-console
- Será redirecionado a uma tela de configuracões inicial onde precisará configurar o caminho do arquivo, usuário e senha
```json
// inicialmente está configurado com esses dados
   {
   	"JDBC_URL":"jdbc:h2:file:./data/database",
   	"username": "admin",
   	"password": "password"
   }
```
## Acesso aos endpoints
- Pode ser utilizado o postman para fazer requisições aos endpoids do projeto ou qualquer outro meio que desejar
- O collection do postman utilizado foi exportado e o arquivo se encontra na pasta raiz deste repositório

## Testes
- Foram criados testes para os endpoints utilizando o JUnit
- Os testes tem seu próprio arquivo de configurações na pasta src/test/java/resources, é o arquivo application.properties
- Eles estão configurados para rodar utilizando o banco em memória mas pode ser alternado para utilizar o banco em arquivo da mesma maneira que a aplicação funcionaria em produção
