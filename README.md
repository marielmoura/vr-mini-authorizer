
# Projeto Mini Autorizador VR - Desafio de programação

Este é o projeto MiniAutorizador de transações da VR, uma aplicação Spring Boot que utiliza Java e Maven.

## Configuração do Ambiente

### Pré-requisitos

- Java 17
- Maven
- Docker

## Configuração do Projeto

As configurações do projeto estão localizadas no arquivo `src/main/resources/application.properties`. Aqui está uma configuração personalizada:

- `vr.miniauthorizer.initial-card-balance`: Saldo inicial para criação de qualquer novo cartão.

## Executando o Projeto

Para executar o projeto via Maven, abra um terminal no diretório raiz do projeto (onde o arquivo `pom.xml` está localizado) e execute o seguinte comando:

```bash
mvn spring-boot:run
```

## Executando o Banco de Dados

O projeto também inclui um arquivo `docker-compose.yml` para a configuração do Docker. Para iniciar o serviço MySQL, execute o seguinte comando no diretório onde o arquivo `docker-compose.yml` está localizado:

```bash
docker-compose up
```
## Endpoints


### POST - Criando um novo cartão

http://localhost:8080/cartoes


```json
{
    "senha": "1234",
    "numeroCartao": "4810239597849161"
}
```
### GET - Obtendo saldo do cartão

http://localhost:8080/cartoes/{numeroDoCartao}


### POST - Criando uma nova transação

http://localhost:8080/transacoes


```json
{
    "senhaCartao": "1234",
    "numeroCartao": "4810239597849161",
    "valor": 450

}
```
## Demais comentários do desenvolvedor

Bom, tentei fazer de forma a delegar para cada camada suas devidas responsabilidades. É simples, mas desafiador sempre. No geral adorei fazer o desafio.

Tomei a liberdade de implementar duas regras adicionais: a de não permitir valores zerados e números incorretos de cartão.

Não consegui usar o docker-compose para subir a aplicação junto com banco por conta de um problema de conectividade entre os containers na minha máquina (mesmo depois de muitas tentativas), mas deixei comentado o código caso consigam por aí.

Também tive que usar alguns `ifs` para tratar exceções, tentei fazer de outras formas mas não consegui, então deixei assim mesmo.

Espero que gostem!