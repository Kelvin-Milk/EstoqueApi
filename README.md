# Estoque API

API RESTful para controle de estoque, desenvolvida como desafio técnico Full Stack. Permite cadastrar produtos, registrar entradas e saídas com validação de saldo e consultar estoque e lucro por produto.

> **Status:** em desenvolvimento. O CRUD de produtos está concluído; movimentações de estoque, consultas e front-end estão nas [próximas etapas](#próximas-etapas).

## Tecnologias

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Java 21 (LTS) |
| Framework | Spring Boot 4.1 (Web MVC, Data JPA, Validation, Actuator) |
| Banco de dados | H2 em memória (padrão) · PostgreSQL (driver incluído) |
| Migrations | Flyway |
| Mapeamento | MapStruct · Lombok |
| Documentação | SpringDoc OpenAPI (Swagger UI) |
| Testes | JUnit 5 · AssertJ · Mockito · MockMvc |
| Build | Maven (wrapper incluído) |

## Como executar

**Pré-requisito:** JDK 21. Não é preciso instalar Maven nem banco de dados.

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. O Flyway cria as tabelas e a carga inicial de tipos de produto automaticamente.

| Recurso | URL |
| --- | --- |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Contrato OpenAPI | http://localhost:8080/v3/api-docs |
| Health check | http://localhost:8080/actuator/health |
| Console do H2 | http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:estoque`, usuário `sa`, sem senha) |

## Endpoints

### Produtos — `/api/v1/produtos`

| Método | Rota | Descrição | Sucesso |
| --- | --- | --- | --- |
| `POST` | `/api/v1/produtos` | Cadastra um produto | `201 Created` + header `Location` |
| `GET` | `/api/v1/produtos` | Lista os produtos | `200 OK` |
| `GET` | `/api/v1/produtos/{id}` | Busca um produto | `200 OK` |
| `PUT` | `/api/v1/produtos/{id}` | Atualiza descrição, tipo e valor no fornecedor | `200 OK` |
| `DELETE` | `/api/v1/produtos/{id}` | Exclui um produto | `204 No Content` |

**Exemplo de cadastro:**

```http
POST /api/v1/produtos
Content-Type: application/json

{
  "codigo": "NB-001",
  "descricao": "Notebook Dell Inspiron",
  "tipoId": 1,
  "valorFornecedor": 3500.00
}
```

```json
{
  "id": 1,
  "codigo": "NB-001",
  "descricao": "Notebook Dell Inspiron",
  "tipo": { "id": 1, "descricao": "Eletrônico" },
  "quantidadeEstoque": 0,
  "valorFornecedor": 3500.00
}
```

Tipos de produto cadastrados na carga inicial: `1` Eletrônico, `2` Eletrodoméstico, `3` Móvel.

### Erros

Todos os erros seguem o formato **`ProblemDetail` (RFC 9457)**:

```json
{
  "type": "about:blank",
  "title": "Dados inválidos",
  "status": 400,
  "detail": "Um ou mais campos estão inválidos",
  "instance": "/api/v1/produtos",
  "campos": { "codigo": "Código é obrigatório" }
}
```

| Status | Quando |
| --- | --- |
| `400 Bad Request` | Campos inválidos ou regra de formato violada |
| `404 Not Found` | Produto ou tipo de produto inexistente |
| `422 Unprocessable Content` | Regra de negócio violada (ex.: código já cadastrado) |

## Arquitetura

Organização em camadas inspirada em DDD, com a dependência apontando sempre para o domínio:

```
api  →  application  →  domain
```

```
src/main/java/com/kelvinleite/estoque
├── api/            Controllers e tratamento global de erros
├── application/    Casos de uso (services), DTOs e mappers
└── domain/         Entidades, regras de negócio, exceções e repositórios
```

O domínio não conhece HTTP nem JSON. Os services apenas orquestram: as regras ficam nas próprias entidades.

## Decisões técnicas

- **Modelo de domínio rico.** O `Produto` não tem setters públicos; as alterações passam por métodos de domínio que validam tudo antes de modificar qualquer campo, evitando objetos em estado inválido.
- **Estoque inicia em zero.** O saldo só muda por movimentações de entrada e saída, garantindo histórico rastreável para as consultas de saída e lucro. Pelo mesmo motivo, `quantidadeEstoque` não é editável no CRUD.
- **Código do produto imutável.** Funciona como identificador de negócio; por isso não existe no request de atualização.
- **Tipo de produto como tabela, não enum.** Categoria é dado cadastral e deve poder crescer sem deploy. Os tipos do enunciado entram como carga inicial na migration. Já o tipo de movimentação (entrada/saída) será enum, porque determina comportamento.
- **Concorrência com lock otimista.** `@Version` no `Produto` impede que operações simultâneas sobrescrevam o saldo umas das outras, sem travar registros no banco.
- **Valores monetários em `BigDecimal`** (`DECIMAL(15,2)` no banco), evitando os erros de arredondamento do `double`.
- **Migrations como fonte de verdade.** O schema é definido pelo Flyway, com constraints nomeadas, `CHECK`, índice na chave estrangeira e carga inicial. O Hibernate apenas valida o mapeamento (`ddl-auto: validate`).
- **Sem N+1 na listagem.** O tipo é carregado em modo `LAZY`, e a listagem usa `@EntityGraph` para trazer tudo em uma única consulta.
- **DTOs separados por operação** (`record`), sem expor a entidade na API. O MapStruct faz o mapeamento entidade → resposta; a criação de entidades é explícita, para preservar as regras do domínio.
- **Tratamento centralizado de erros** com `@RestControllerAdvice` e `ProblemDetail`: o domínio lança exceções de negócio sem conhecer HTTP, e nenhum detalhe interno é exposto ao cliente.
- **API versionada** (`/api/v1`) e `open-in-view` desativado.

## Testes

```bash
./mvnw test        # Windows: mvnw.cmd test
```

| Nível | Classe | O que cobre |
| --- | --- | --- |
| Domínio | `ProdutoTest` | Invariantes do produto, sem Spring nem banco |
| Aplicação | `ProdutoServiceTest` | Orquestração do service com repositórios mockados (Mockito) |
| Web | `ProdutoControllerTest` | Contrato HTTP, validação e formato de erro (`@WebMvcTest`) |
| Integração | `EstoqueApiApplicationTests` | Subida completa da aplicação (smoke test) |

## Próximas etapas

- [ ] Entidade `MovimentoEstoque` com registro de entrada e saída
- [ ] Validação de saldo insuficiente na saída, com mensagem específica
- [ ] Consulta de produtos por tipo, com quantidade de saída e quantidade disponível
- [ ] Consulta de lucro por produto (quantidade total de saída e lucro total)
- [ ] Front-end em Vue.js
- [ ] Docker Compose com PostgreSQL
- [ ] Pipeline de CI com GitHub Actions

## Uso de IA

Utilizei IA como apoio durante o desenvolvimento, principalmente para tirar dúvidas da stack Java/Spring (minha experiência principal é em .NET) e para revisar o código. As decisões de arquitetura e modelagem foram minhas e estão justificadas na seção [Decisões técnicas](#decisões-técnicas).

## Commits

O histórico segue o padrão [Conventional Commits](https://www.conventionalcommits.org/), com commits pequenos por funcionalidade.