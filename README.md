# Sobre o sistema:
O Sistema Findify foi desenvolvido para auxiliar nas buscas por pessoas desaparecidas, contando com campos de informações caracteristicas sobre a pessoa desaparecida e também onde ele foi visto pela ultima vez, nosso sistema busca ajudar as familias que tem seus entes queridos desaparecidos com informações uteis que podem ajudar na busca.

# Tecnologias:
- [x] **Java Spring Boot: Para a API backend.**
- [x] **Android Studio: Para o desenvolvimento do aplicativo Android.**
- [x] **MariaDB (MySQL): Banco de dados utilizado.**
- [x] **Host: Serviço de hospedagem (VPS Hostinger).**

# Instruções de Execução:
Inicie o aplicativo, na tela incial voce encontrará 3 botões funcionais, de acordo com sua necessidade clique em um deles. Caso escolha o de ver desaparecidos você será redirecionado para uma tela onde serão listadas todas as pessoas desaparecidas, incluindo suas informações,  e um botão que irá redireciona-lo para tela de cadastrar o avistamento da respectiva pessoa. Caso escolha o botão de cadastrar desaparecido, você será redirecionado para uma tela, onde encontrará um formulário e nele poderá preencher com as informações sobre a pessoa desaparecida. Por fim, caso escolha o botão de visualizar avistamento, você será redirecionado para uma tela onde encontrará novamente uma lista de desaparecidos, com botões de redirecionamento em cada um deles, e ao clicar em um dos botões, será redirecionado para outra tela onde encontrará uma nova lista, dessa vez com informações dos últimos avistamentos do desaparecido selecionado.

# Requisitos Funcionais:
- [x] **Cadastrar desaparecido**
- [x] **Cadastrar avistamento**
- [x] **Visualizar todos desaparecidos**
- [x] **Visualizar ultimos avistamentos**
- [x] **Registrar informações do desaparecido**

# API e Funcionalidades

A API tem como principal foco a gestão de desaparecidos e avistamentos. Ela oferece endpoints para criar, listar, atualizar e deletar registros de desaparecidos, além de adicionar e listar avistamentos. Abaixo está um resumo das principais classes e funcionalidades da API:

## Classes Controladoras

- **Controller**
  - `@RestController` e `@RequestMapping("/api-a3")`: Define a classe como um controlador REST com o mapeamento base `/api-a3`.
  - **Endpoints**:
    - `GET /api-a3/testarConexaoDb`: Testa a conexão com o banco de dados.
    - `POST /api-a3/cadastrarDesaparecido`: Cadastra um novo desaparecido com a opção de upload de imagem. A imagem é comprimida antes de ser armazenada.
    - `POST /api-a3/adicionarAvistamento`: Adiciona um novo avistamento para um desaparecido existente.
    - `GET /api-a3/listarAvistamentosPorIdDesaparecido`: Lista avistamentos para um determinado desaparecido.
    - `GET /api-a3/listarDesaparecidos`: Retorna uma lista de todos os desaparecidos cadastrados.
    - `GET /api-a3/puxarFotoDesaparecido/{id}`: Retorna a foto de um desaparecido específico.
    - `GET /api-a3/PuxarDesaparecidoPorId/{id}`: Retorna os detalhes de um desaparecido específico (sem a foto).
    - `PUT /api-a3/atualizarDesaparecido/{id}`: Atualiza os dados de um desaparecido existente.
    - `DELETE /api-a3/deletarDesaparecido/{id}`: Deleta um desaparecido pelo seu ID.

## Classes de Entidade

- **Desaparecido**
  - `@Entity` e `@Table(name = "desaparecidos")`: Define a classe como uma entidade JPA com a tabela correspondente `desaparecidos`.
  - Atributos principais: `id`, `nome`, `dataNascimento`, `descricao`, `status`, `imagem`.

- **Avistamento**
  - `@Entity` e `@Table(name = "avistamentos")`: Define a classe como uma entidade JPA com a tabela correspondente `avistamentos`.
  - Atributos principais: `id`, `desaparecido` (relação `@ManyToOne` com Desaparecido), `comentario`.

## Repositórios

- **DesaparecidoRepository**
  - `extends JpaRepository<Desaparecido, Long>`: Interface de repositório para a entidade Desaparecido, fornecendo métodos CRUD padrão.

- **AvistamentoRepository**
  - `extends JpaRepository<Avistamento, Long>`: Interface de repositório para a entidade Avistamento, com métodos personalizados para buscar avistamentos por ID de desaparecido e inserir novos avistamentos.

## Serviços

- **DesaparecidoService**
  - Interface definindo os métodos: `createDesaparecido`, `getAllDesaparecidos`, `getDesaparecidoById`, `updateDesaparecido`, `deleteDesaparecido`.

- **DesaparecidoServiceImpl**
  - Implementação da interface `DesaparecidoService`, utilizando o repositório `DesaparecidoRepository` para realizar operações no banco de dados.

- **AvistamentoService**
  - Interface definindo os métodos: `createAvistamento`, `getAvistamentosByDesaparecidoId`.

- **AvistamentoServiceImpl**
  - Implementação da interface `AvistamentoService`, utilizando o repositório `AvistamentoRepository` para realizar operações no banco de dados.

## Funcionalidades Adicionais

- **comprimirImagem(MultipartFile arquivo)**
  - Método auxiliar para comprimir imagens usando o formato JPEG2000, reduzindo o espaço ocupado no banco de dados.

## Resumo dos Endpoints

- `GET /api-a3/testarConexaoDb`: Testa a conexão com o banco de dados.
- `POST /api-a3/cadastrarDesaparecido`: Cadastra um novo desaparecido com a opção de upload de imagem.
- `POST /api-a3/adicionarAvistamento`: Adiciona um novo avistamento.
- `GET /api-a3/listarAvistamentosPorIdDesaparecido`: Lista avistamentos por ID de desaparecido.
- `GET /api-a3/listarDesaparecidos`: Lista todos os desaparecidos.
- `GET /api-a3/puxarFotoDesaparecido/{id}`: Obtém a foto de um desaparecido específico.
- `GET /api-a3/PuxarDesaparecidoPorId/{id}`: Obtém detalhes de um desaparecido específico.
- `PUT /api-a3/atualizarDesaparecido/{id}`: Atualiza os dados de um desaparecido.
- `DELETE /api-a3/deletarDesaparecido/{id}`: Deleta um desaparecido pelo ID.
