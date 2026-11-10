# CitizenTech - Cidade em Alerta

API RESTful (Spring Boot 3 + Java 21 + Oracle FIAP) para previsão e prevenção de riscos urbanos.
Cruza sensores, relatos de moradores, histórico e capacidade de escoamento para gerar o
**Alerta Antecipado por Microrregião**, com probabilidade e **nível de confiança**.

## Arquitetura (camadas)

| Pacote | Responsabilidade |
|---|---|
| `model` / `model.enums` | Entidades JPA mapeadas nas tabelas Oracle |
| `dao` | Acesso a dados (Spring Data JPA) |
| `bo` | Regras de negócio e cálculo de risco (`CalculadoraRisco`) |
| `dto` | Objetos de entrada/saída da API com Bean Validation |
| `resource` | Controllers REST (GET, POST, PUT, DELETE) |
| `exception` | Exceções customizadas + `GlobalExceptionHandler` (JSON padronizado) |

## Requisitos
- Java JDK 21, Maven 3.9+ (ou Docker), acesso ao Oracle da FIAP, IntelliJ IDEA (recomendado).

## Banco de dados
Execute no SQL Developer, no schema do grupo, nesta ordem:
1. `database/01_create_tables.sql`
2. `database/02_seed_dados.sql` (dados fictícios; rode pouco antes de demonstrar)

`database/00_drop_tables.sql` apaga tudo para reiniciar.

## Variáveis de ambiente
| Variável | Descrição | Padrão |
|---|---|---|
| `DB_URL` | URL JDBC | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |
| `DB_USER` | Usuário (RM) | (obrigatória) |
| `DB_PASSWORD` | Senha | (obrigatória) |
| `DDL_AUTO` | `update`, `validate` ou `none` | `update` |
| `PORT` | Porta HTTP | `8080` |

## Rodar localmente
```bash
export DB_USER=RM00000
export DB_PASSWORD=sua_senha
mvn spring-boot:run
```
Windows (PowerShell): `$env:DB_USER="RM00000"; $env:DB_PASSWORD="sua_senha"; mvn spring-boot:run`

- Swagger: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/actuator/health

## Rodar com Docker
```bash
docker build -t citizentech .
docker run -p 8080:8080 -e DB_USER=RM00000 -e DB_PASSWORD=sua_senha citizentech
```

## Deploy no Render
1. Suba o projeto no GitHub.
2. No Render: **New > Web Service**, conecte o repositório e escolha **Runtime: Docker**.
3. Em **Environment**, cadastre `DB_USER`, `DB_PASSWORD` (e `DB_URL`, se diferente do padrão).
4. Em **Health Check Path**, informe `/actuator/health/liveness`.
5. Deploy. Abra `https://SEU-SERVICO.onrender.com/swagger-ui.html`.

## Alerta Antecipado
`POST /alertas/calcular/{microRegiaoId}`

Probabilidade = 30% chuva + 20% histórico + 20% (100 − escoamento) + 20% nível da água + 10% relatos.
Confiança = 25 base + 25 (há sensor) + 20 (há relatos recentes) + 15 (há histórico) + até 15 (atualidade da leitura).
Níveis: BAIXO < 25, MODERADO < 50, ALTO < 75, CRITICO ≥ 75.
`GET /micro-regioes/prioridade` devolve o ranking de atendimento.

Exemplos prontos de requisição em `docs/colecao-requisicoes.http`.
