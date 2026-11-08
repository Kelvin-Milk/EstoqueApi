# Estoque API

Aplicação full stack para controle de estoque, desenvolvida como desafio técnico. Permite cadastrar produtos, registrar entradas e saídas com validação de saldo e consultar estoque e lucro por produto.

> **Status:** em desenvolvimento. Back-end com CRUD de produtos, movimentação de estoque e listagem de tipos concluídos; front-end em construção e consultas de estoque e lucro nas [próximas etapas](#próximas-etapas).

## Tecnologias

| Camada | Tecnologia |
| --- | --- |
| Back-end | Java 21 (LTS) · Spring Boot 4.1 (Web MVC, Data JPA, Validation, Actuator) |
| Banco de dados | H2 em memória (padrão) · PostgreSQL (driver incluído) |
| Migrations | Flyway |
| Mapeamento | MapStruct · Lombok |
| Documentação da API | SpringDoc OpenAPI (Swagger UI) |
| Testes do back-end | JUnit 5 · AssertJ · Mockito · MockMvc |
| Front-end | Vue 3 · Vite · TypeScript · Vue Router · Pinia · Axios |
| Build | Maven (wrapper incluído) · npm |

## Como executar

### Back-end

**Pré-requisito:** JDK 21. Não é preciso instalar Maven nem banco de dados.

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A API sobe em `http://localhost:8080`. O Flyway cria as tabelas e a carga inicial de tipos de produto automaticamente.

| Recurso | URL |
| --- | --- |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Contrato OpenAPI | http://localhost:8080/v3/api-docs |
| Health check | http://localhost:8080/actuator/health |
| Console do H2 | http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:estoque`, usuário `sa`, sem senha) |

### Front-end

**Pré-requisito:** Node.js 24 LTS (ou 22.18+). Com o back-end rodando:

```bash
cd frontend
npm install
npm run dev
```

O front-end sobe em `http://localhost:5173`. Em desenvolvimento, o Vite repassa as chamadas para `/api` ao back-end na porta 8080 (proxy), sem necessidade de configurar CORS.

## Endpoints

### Produtos — `/api/v1/produtos`

| Método | Rota | Descrição | Sucesso |
| --- | --- | --- | --- |
| `POST` | `/api/v1/produtos` | Cadastra um produto | `201 Created` + header `Location` |
| `GET` | `/api/v1/produtos` | Lista os produtos | `200 OK` |
| `GET` | `/api/v1/produtos/{id}` | Busca um produto | `200 OK` |
| `PUT` | `/api/v1/produtos/{id}` | Atualiza descrição, tipo e valor no fornecedor | `200 OK` |
| `DELETE` | `/api/v1/produtos/{id}` | Exclui um produto sem movimentações | `204 No Content` |

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

### Movimentação de estoque — `/api/v1/produtos/{id}`

| Método | Rota | Descrição | Sucesso |
| --- | --- | --- | --- |
| `POST` | `/api/v1/produtos/{id}/entradas` | Registra entrada (soma ao saldo) | `201 Created` |
| `POST` | `/api/v1/produtos/{id}/saidas` | Registra saída (valida e subtrai do saldo) | `201 Created` |

**Exemplo de saída:**

```http
POST /api/v1/produtos/1/saidas
Content-Type: application/json

{ "quantidade": 4, "valorVenda": 4500.00 }
```

```json
{
  "id": 2,
  "produtoId": 1,
  "tipo": "SAIDA",
  "quantidade": 4,
  "valorVenda": 4500.00,
  "custoUnitario": 3500.00,
  "dataMovimentacao": "2026-11-08T17:40:12",
  "saldoAtual": 6
}
```

Na entrada, o corpo é apenas `{ "quantidade": 10 }` e o `valorVenda` vem nulo. O `valorVenda` é unitário.

**Saldo insuficiente:**

```json
{
  "title": "Regra de negócio não respeitada",
  "status": 422,
  "detail": "Estoque insuficiente para o produto NB-001. Disponível: 6, solicitado: 15"
}
```

### Tipos de produto — `/api/v1/tipos-produto`

| Método | Rota | Descrição | Sucesso |
| --- | --- | --- | --- |
| `GET` | `/api/v1/tipos-produto` | Lista os tipos em ordem alfabética | `200 OK` |

Carga inicial: `1` Eletrônico, `2` Eletrodoméstico, `3` Móvel.

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
| `400 Bad Request` | Campos inválidos ou valores fora das regras de formato |
| `404 Not Found` | Produto ou tipo de produto inexistente |
| `409 Conflict` | Conflito com o estado atual: alteração simultânea do mesmo produto ou violação de integridade |
| `422 Unprocessable Content` | Regra de negócio violada: saldo insuficiente, código já cadastrado, exclusão de produto com movimentações |

## Arquitetura

Organização em camadas inspirada em DDD, com a dependência apontando sempre para o domínio:

```
api  →  application  →  domain  ←  infrastructure
```

```
src/main/java/com/kelvinleite/estoque
├── api/              Controllers e tratamento global de erros
├── application/      Casos de uso (services), DTOs e mappers
├── domain/           Entidades, regras de negócio, exceções e repositórios
└── infrastructure/   Configurações técnicas
frontend/             Aplicação Vue 3
```

O domínio não conhece HTTP nem JSON. Os services apenas orquestram: as regras ficam nas próprias entidades.

## Decisões técnicas

### Domínio
- **Modelo de domínio rico.** O `Produto` não tem setters públicos; as alterações passam por métodos de domínio que validam tudo antes de modificar qualquer campo, evitando objetos em estado inválido.
- **Movimentação criada apenas pelo produto.** Entradas e saídas nascem por `produto.registrarEntrada()` e `produto.registrarSaida()`. As fábricas do `MovimentoEstoque` são visíveis só no pacote do produto, então o compilador garante que não existe saída sem baixa no saldo.
- **Estoque inicia em zero.** O saldo só muda por movimentações, garantindo histórico rastreável para as consultas de saída e lucro. Pelo mesmo motivo, `quantidadeEstoque` não é editável no CRUD.
- **Custo registrado no movimento.** Cada saída guarda o valor do fornecedor daquele momento (`custoUnitario`), para que o lucro histórico não mude quando o custo do produto for alterado.
- **Produtos com movimentações não podem ser excluídos**, para preservar o histórico. Uma evolução natural seria a exclusão lógica (inativação).
- **Código do produto imutável.** Funciona como identificador de negócio; por isso não existe no request de atualização.
- **Tipo de produto como tabela, tipo de movimentação como enum.** Categoria é dado cadastral e deve poder crescer sem deploy; entrada e saída determinam comportamento no código.

### Persistência e concorrência
- **Lock otimista.** `@Version` no `Produto` impede que operações simultâneas sobrescrevam o saldo; saldo e movimento são gravados na mesma transação, e um conflito desfaz os dois.
- **Valores monetários em `BigDecimal`** (`DECIMAL(15,2)` no banco), evitando os erros de arredondamento do `double`.
- **Migrations como fonte de verdade.** O schema é definido pelo Flyway, com constraints nomeadas, `CHECK` (quantidade positiva, valor de venda obrigatório só na saída), índices e carga inicial. O Hibernate apenas valida o mapeamento (`ddl-auto: validate`).
- **Sem N+1 na listagem.** O tipo é carregado em modo `LAZY`, e a listagem usa `@EntityGraph` para trazer tudo em uma única consulta.

### API
- **DTOs separados por operação** (`record`), sem expor a entidade. O MapStruct faz o mapeamento entidade → resposta; a criação de entidades é explícita, para preservar as regras do domínio.
- **Tratamento centralizado de erros** com `@RestControllerAdvice` e `ProblemDetail`: regras previsíveis respondem 422; conflitos imprevisíveis (concorrência, integridade) respondem 409. Nenhum detalhe interno é exposto ao cliente.
- **Data da movimentação definida pelo servidor**, via `Clock` injetado (testável).
- **API versionada** (`/api/v1`) e `open-in-view` desativado.

## Testes

```bash
./mvnw test        # Windows: mvnw.cmd test
```

| Nível | Classe | O que cobre |
| --- | --- | --- |
| Domínio | `ProdutoTest` | Invariantes do produto, sem Spring nem banco |
| Domínio | `ProdutoMovimentacaoEstoqueTest` | Entrada, saída, saldo insuficiente (incluindo o limite exato do saldo) e custo registrado |
| Aplicação | `ProdutoServiceTest` | Orquestração do service com repositórios mockados (Mockito) |
| Web | `ProdutoControllerTest` · `TipoProdutoControllerTest` | Contrato HTTP, validação e formato de erro (`@WebMvcTest`) |
| Integração | `EstoqueApiApplicationTests` | Subida completa da aplicação, migrations e mapeamento (smoke test) |

## Próximas etapas

- [x] CRUD de produtos
- [x] Registro de entrada e saída com validação de saldo e mensagem específica
- [x] Listagem de tipos de produto
- [ ] Consulta de produtos por tipo, com quantidade de saída e quantidade disponível
- [ ] Consulta de lucro por produto (quantidade total de saída e lucro total)
- [ ] Front-end: telas de produtos, movimentação e consultas
- [ ] Docker Compose com PostgreSQL
- [ ] Pipeline de CI com GitHub Actions

## Uso de IA

Utilizei IA como apoio durante o desenvolvimento, principalmente para tirar dúvidas da stack Java/Spring (minha experiência principal é em .NET) e para revisar o código. As decisões de arquitetura e modelagem foram minhas e estão justificadas na seção [Decisões técnicas](#decisões-técnicas).

## Commits

O histórico segue o padrão [Conventional Commits](https://www.conventionalcommits.org/), com commits pequenos por funcionalidade.
