# Vagaí · Backend Java

Global Solution FIAP 2026/2 · CitizenTech. A primeira versão permite pesquisar estacionamentos por rua e apresentar uma lista com nome, bairro, número, tipo e tarifas. Os tipos são GRATUITO, ROTATIVO e PRIVADO. Motoristas autenticados cadastram locais, salvam favoritos, avaliam e registram relatos.

## Executar no IntelliJ

1. Abra o pom.xml como projeto Maven e selecione JDK 21.
2. Execute `./mvnw verify`, ou `.\mvnw.cmd verify` no Windows. Os testes usam Spring MVC, sessões e DAOs reais com H2 em modo Oracle.
3. Abra um esquema Oracle reservado ao Vagaí no SQL Developer. Execute [01_estrutura.sql](sql/01_estrutura.sql), a carga opcional [02_dados_teste.sql](sql/02_dados_teste.sql) e [03_consultas.sql](sql/03_consultas.sql).
4. Configure ORACLE_URL, ORACLE_USER e ORACLE_PASSWORD nas variáveis de ambiente da execução de VagaiApplication.
5. Inicie VagaiApplication ou execute `.\mvnw.cmd spring-boot:run`. Consulte `GET http://localhost:8080/estacionamentos?rua=Rua%20de%20Teste`.

A carga contém 72 inserts fictícios, um UPDATE e um DELETE com WHERE. Restam 71 registros. Cadastre sua conta pela API para fazer login. Os hashes das contas da carga correspondem a valores aleatórios descartados. A estrutura inicial destina-se a esquema vazio.

## Configuração

| Variável | Uso |
|---|---|
| ORACLE_URL | JDBC Thin fornecido pela FIAP, como jdbc:oracle:thin:@//HOST:1521/SERVICO ou no formato SID indicado pela instituição. |
| ORACLE_USER e ORACLE_PASSWORD | Conta e senha do esquema, configuradas no ambiente. |
| PORT | Porta HTTP, padrão 8080. |
| FRONTEND_ORIGIN | Origens exatas separadas por vírgula, sem barra final. Padrão: http://localhost:5173. |
| COOKIE_SECURE e COOKIE_SAME_SITE | HTTP local usa false e lax. HTTPS entre sites diferentes usa true e none. |

## Cadastro e lista

`GET /estacionamentos?rua=Flores&bairro=Centro` retorna os três tipos ativos que correspondem ao filtro. O bairro é opcional. A pesquisa ignora maiúsculas e minúsculas, considera acentos e aceita parte do nome. Sem filtros, lista todos os locais ativos. A ordem é rua, bairro, número como texto e ID.

```json
{
  "nome": "Garagem Exemplo",
  "tipo": "PRIVADO",
  "rua": "Rua das Flores",
  "bairro": "Centro",
  "numero": "120A",
  "primeiraHora": 18,
  "demaisHoras": 8,
  "ativo": 1
}
```

Envie esse corpo em POST /estacionamentos ou PUT /estacionamentos/{id}, com sessão autenticada. Rua, bairro e número são obrigatórios. numero aceita 120A e s/n. Em trechos gratuitos ou rotativos, informe o número de referência.

Tarifas em reais. primeiraHora indica o total da primeira hora. demaisHoras indica o valor de cada hora adicional. O cadastro manual privado exige a tarifa da primeira hora e o valor das demais horas. GRATUITO aceita somente zero ou null. Eventuais cobranças de frações devem constar na descrição.

[Contrato JSON completo](docs/API.md) · [Coleção Postman](docs/Vagai.postman_collection.json)

## Banco e horários

São seis tabelas, seis sequences e oito FKs. As sequences seguem a exigência original e a compatibilidade com o Oracle disponibilizado à equipe. Avaliações e favoritos têm UNIQUE (id_usuario, id_estacionamento). Cada usuário mantém uma nota por local, editável com PUT.

TB_USUARIO usa ds_senha_hash com PBKDF2 e salt individual. As datas de cadastro, reporte, avaliação e favorito têm DEFAULT SYSDATE. A API grava UTC. Inserções manuais que usam o default devem considerar o fuso do Oracle. O índice de reportes usa (id_estacionamento, dt_reporte). Reportes perdem validade informativa após dez minutos.

TB_ESTACIONAMENTO concentra o endereço e as duas tarifas. st_ativo é padronizado nas tabelas de locais e regras, com 1 para ativo e 0 para inativo. A exclusão de usuário deixa o criador do local nulo. Reportes, avaliações e favoritos bloqueiam a exclusão da conta com 409. Todas as dependências de um estacionamento bloqueiam sua exclusão. Um local sem criador fica consultável e sem usuário autorizado a editá-lo.

TB_REGRA_ESTACIONAMENTO concentra horários e restrições. diaSemana vai de 1 (segunda) a 7 (domingo). inicio e fim são minutos desde 00h00, entre 0 e 1439. Segunda das 20h a terça às 6h usa diaSemana=1, inicio=1200, fim=360. Início incluído, fim excluído. Valores iguais representam 24 horas desde o início.

Cadastre uma linha por dia de início. `GET /estacionamentos/{id}/regras?diaSemana=2&minuto=1260` retorna regras ativas aplicáveis a terça às 21h, incluindo intervalos iniciados no dia anterior. Cada regra indica PERMITIDO ou PROIBIDO. Lista vazia significa ausência de informação. Em conflito, a apresentação deve priorizar PROIBIDO. Horários são locais ao estacionamento.

## Arquitetura e endpoints

Java 21, Spring Boot 4.1.1, Maven, JDBC, DataSource, PreparedStatement, Bean Validation e ModelMapper. Models e DTOs têm atributos privados e métodos de acesso. DTOs de entrada usam RequestDto e os de saída ResponseDto, seguindo a referência GAIA. ErroResponseDto padroniza o corpo de erros. Relacionamentos no model usam objetos com identidade, e o JSON usa IDs.

| Pacote | Responsabilidade |
|---|---|
| model e dto | Entidades e contratos. Regra avalia intervalos e Reporte calcula validade. |
| dao | Um DAO independente por entidade, ConnectionFactory, PreparedStatement e fechamento de recursos. |
| controller | CRUD, autorização, conversão e status HTTP. |
| config | Autenticacao, Senha, CorsFilter e ModelMapperConfig, cada um em seu arquivo. |
| bo | UsuarioBo para cadastro, login e atualização. |
| factory | ConnectionFactory obtém conexões e participa das transações. |
| exception | Exceções com sufixo Exception, ApiExceptionHandler e tratamento de erros do servidor. |

POST /usuarios, /usuarios/login e /usuarios/logout. GET /usuarios/me. Os seis recursos /usuarios, /estacionamentos, /reportes, /avaliacoes, /regras e /favoritos têm GET/POST na coleção e GET/PUT/DELETE em /{id}. GET /usuarios mostra a própria conta. Favoritos são restritos à sessão.

GET/POST /estacionamentos/{id}/reportes e /avaliacoes. GET /estacionamentos/{id}/regras. GET /usuarios/me/favoritos.

## React e Postman

No React, envie `credentials: "include"` no login e nas chamadas autenticadas. O cookie de sessão é HttpOnly. O login renova a sessão. Para acessar a mesma API publicada pelo site e pelo Vite local, configure FRONTEND_ORIGIN=https://vagai-three.vercel.app,http://localhost:5173 no Render.

Importe a coleção no Postman, preencha baseUrl, nome e senha e mantenha o cookie jar habilitado. O Runner captura IDs e demonstra CRUD, conflitos, 400 e 404. Use um nome novo para cada execução completa.

## Docker e Render

Use `docker build -t vagai .`. Com ORACLE_URL, ORACLE_USER e ORACLE_PASSWORD exportadas, execute `docker run --rm -p 8080:8080 -e ORACLE_URL -e ORACLE_USER -e ORACLE_PASSWORD vagai`. O Dockerfile compila com Maven/JDK 21 e executa em JRE 21.

No Render, crie um Web Service Docker, informe a raiz do backend e as variáveis do Oracle e frontend. Verifique a conectividade com a FIAP. Para frontend HTTPS em outro site, use COOKIE_SECURE=true e COOKIE_SAME_SITE=none. A sessão fica na memória de uma instância e termina em reinícios. [Docker no Render](https://render.com/docs/docker)

## Validação e entrega

A revisão da lista foi verificada com Spring MockMvc e H2 em modo Oracle, exercitando controllers, sessões, validação, CORS, SQL e DAOs. O ambiente bloqueou conexões locais do Tomcat, então esta revisão usa despacho de requisições em memória. A suíte executa os scripts de criação, carga e dez consultas. Verifica os três tipos por rua, bairro, tarifas, autoria, duplicidade de avaliações e favoritos, exclusão de conta, regras noturnas.

Oracle FIAP, imagem Docker, Render e integração React exigem validação externa. [Documentação técnica](docs/Documentacao-tecnica.pdf)

Integrantes, RMs e turma aguardam os dados da equipe. Publicação e vídeo aguardam links reais.

Roteiro de até cinco minutos. 0:00–0:35 problema. 0:35–1:25 login e pesquisa por rua. 1:25–2:20 lista com tipos e tarifas, reporte e favorito. 2:20–3:20 CRUD no Postman, 400 e 404. 3:20–4:15 controller, DTO, DAO e Oracle. 4:15–5:00 DER, regras e URL publicada.

Uso de IA. O Codex auxiliou a implementação, scripts, testes e documentação. A equipe deve revisar e reproduzir a demonstração.

GET, PUT e DELETE em /usuarios/me consultam, atualizam e excluem a conta da sessão. POST /usuarios/logout exige sessão ativa (204 ou 401). Os caminhos anteriores sob /auth continuam disponíveis para compatibilidade.
