# LoL History REST API

## Descrição
Projeto de API REST desenvolvido em Java focado na implementação dos pilares da Programação Orientada a Objetos.
O sistema modela entidades básicas relacionadas a partidas de League of Legends, permitindo operações CRUD.

## Tecnologias Utilizadas
- Java 21
- Maven
- H2 Embedded Database
- Servidor HTTP nativo do JDK

## Pré-requisitos
- JDK 21 ou superior
- Maven
- Git

## Como Compilar
```bash
mvn clean compile
```

## Como Executar
- `mvn exec:java -Dexec.mainClass=br.com.jean.lolapirest.App`

ou

- Rodar a classe `App.java` localizada em `lol-api-rest/src/main/java/br/com/jean/lolapirest/App.java`.

**A aplicação roda na porta 8080.**

## Estrutura do Projeto
- `controller/` — endpoints HTTP
- `dao/` — acesso ao banco de dados
- `model/` — entidades e enums
- `config/` — configuração de conexão com o banco

## Endpoints

### Jogadores
- POST `/jogadores`
- GET `/jogadores/{id}`
- PUT `/jogadores/{id}`
- DELETE `/jogadores/{id}`

### Campeões
- POST `/campeoes`
- GET `/campeoes/{id}`
- PUT `/campeoes/{id}`
- DELETE `/campeoes/{id}`

### Itens
- POST `/itens`
- GET `/itens/{id}`
- PUT `/itens/{id}`
- DELETE `/itens/{id}`

### Runas
- POST `/runas`
- GET `/runas/{id}`
- PUT `/runas/{id}`
- DELETE `/runas/{id}`

### Partidas
- POST `/partidas`
- GET `/partidas/{id}`

## Banco de Dados
- H2 em modo arquivo persistente
- Tabelas: jogador, campeao, item, runa, partida, partida_item, partida_runa
- Relações:
  - 1..N (jogador : partida, campeão : partida)
  - N..N (partida : item, partida : runa)

## Como Testar
- Usar Postman
- Valores de qualquer tipo devem estar entre aspas:
```json
{
    "exemploString":"String1",
    "exemploInt":"1"
}
```
- Exemplos de requisições para cada endpoint:

**POST ITEM**
```json
{
    "nome":"Rabadon",
    "custo":"3600",
    "tipo_item":"LENDARIO"
}
```
**POST RUNA**
```json
{
    "nome":"Cometa Arcano",
    "caminho":"FEITICARIA",
    "tipo_runa":"CHAVE"
}
```
**POST JOGADOR**
```json
{
    "apelido":"Iid",
    "regiao":"BR"
}
```
**POST PARTIDA RANQUEADA**
```json
{
  "jogador_id":"1",
  "campeao_id":"1",
  "duracao_min":"46",
  "role":"MID",
  "abates":"12",
  "mortes":"4",
  "assistencias":"5",
  "resultado":"VITORIA",
  "tipo":"RANQUEADA",
  "pdl_variacao":"18",
  "itens":"1",
  "runas":"1"
}
```
**POST PARTIDA CASUAL**
```json
{
  "jogador_id":"1",
  "campeao_id":"1",
  "duracao_min":"30",
  "role":"SUP",
  "abates":"5",
  "mortes":"2",
  "assistencias":"17",
  "resultado":"VITORIA",
  "tipo":"CASUAL",
  "modo_jogo":"NORMAL GAME",
  "itens":"1",
  "runas":"1"
}
```