# Desafio Pitang API

Este projeto é uma API RESTful desenvolvida para o Desafio Pitang. Ela permite o cadastro, login e gerenciamento de usuários e carros. O backend foi desenvolvido com Spring Boot, enquanto o front-end foi construído com Angular.


## 🚀 Tecnologias Utilizadas

- **Backend:**
    - **Java 17**: JDK utilizada no projeto.
    - **Spring Boot**: Framework Java para desenvolvimento de APIs REST.
    - **Spring Security**: Autenticação com JWT.
    - **Hibernate / JPA**: ORM.
    - **H2**: Banco de dados em memória.
    - **JUnit**: Para testes unitários.
- **Front-end:**
    - **Angular 18**: Front-end do projeto.
    - **PrimeNG**: Componentes UI
- **Infraestrutura e Ferramentas::**
    - **Lombok**: Redução de boilerplate no Java.
    - **Git**: Versionamento.


## 📌 Como Executar o Projeto

### Pré-requisitos

- Node 18: Para Rodar o Front-end
- JDK 17+:  Para Rodar o back-end

### Passos para rodar

1. **Clone o repositório**:
    ```bash
    git clone git@github.com:robsonalcantara/pitang-api.git
    ```

2. **Abrir ide (eclipse/intellij)**

3. **Configurar o ambiente para utilizar o jdk-17**
    
4.**Inicialize a aplicação DesafioPitangApiApplication** no endereço:

5.**URL da API** 
    ```
    http://localhost:8080/api
    ```


##  Estórias de Usuário

### 1. **Autenticação e Gerenciamento de Usuários ** 👤

- **<ins>1.1. Autenticação**: Como novo usuário, quero me cadastrar no sistema informando meus dados pessoais para criar um perfil e acessar a aplicação.
- **<ins>1.2. Ver Meu Perfil**: Como usuário autenticado, quero ver meu perfil completo para verificar e acompanhar minhas informações.
- **<ins>1.3. Atualizar Usuário**: Como usuário autenticado, quero atualizar os dados pessoais de um usuário para manter o perfil sempre atualizado.
- **<ins>1.4. Visualizar Usuários**: Como usuário, quero ver uma lista de todos os usuários cadastrados e visualizar os detalhes de um usuário específico para gerenciar as informações de cada um.
- **<ins>1.5. Remover Usuário**: Como usuário, quero remover um usuário específico do sistema para manter os dados dos usuários atualizados e organizados.


### 2. **Gerenciamento de Carros**🚗

- **<ins>2.1. Registrar Carro no Perfil**: Como usuário autenticado, quero registrar um carro no meu perfil para ter os dados do meu veículo associados a mim.
- **<ins>2.2. Visualizar Carros**: Como usuário autenticado, quero poder listar os meus carros e visualizar as informações de um deles para ter completas sobre ele.
- **<ins>2.3. Atualizar um Carro**: Como usuário autenticado, quero atualizar as informações de um carro meu para manter o carro sempre atualizado.
- **<ins>2.4. Remover Carro do Perfil**: Como usuário autenticado, quero remover um carro específico do meu perfil para que minhas informações de veículos estejam sempre atualizadas.


## Endpoints

### 1. **Autenticação**

- **POST <ins>/api/signin**: Realiza o login do usuário e retorna um token JWT.
    - **Body**:
        ```json
        {
          "login": "usuario@exemplo.com",
          "password": "senha"
        }
        ```

- **POST <ins>/api/users**: Realiza o cadastro de um novo usuário.
    - **Body**:
        ```json
        {
          "firstName": "test",
          "lastName": "pitang",
          "email": "test@gmail.com",
          "birthday": "1990-04-01",
          "login": "test-pitang",
          "password": "pitang123",
          "phone": "81900000000",
          "cars": [
             {
             "year": 2018,
             "licensePlate": "PDV-5415",
             "model": "CRETA",
             "color": "BLACK"
             }
          ]
        }
  
        ```

### 2. **Usuários**

- **GET <ins>/api/users**: Retorna todos os usuários.
- **GET <ins>/api/users/{id}**: Retorna um usuário específico pelo ID.
- **PUT <ins>/api/users/{id}**: Atualiza as informações de um usuário.
    - **Body**:
        ```json
        {
          "firstName": "test",
          "lastName": "pitang",
          "email": "test@gmail.com",
          "birthday": "1990-04-01",
          "login": "test-pitang",
          "phone": "81900000000"
        }
        ```
- **DELETE <ins>/api/users/{id}**: Deleta um usuário específico.

### 3. **Carros**

- **POST <ins>/api/cars**: Cadastra um carro para o usuário autenticado.
    - **Body**:
        ```json
        {
        "year": 2018,
        "licensePlate": "PDV-5415",
        "model": "CRETA",
        "color": "BLACK"
        }
        ```

- ** GET <ins>/api/cars**: Retorna todos os carros do usuário autenticado.
- ** GET <ins>/api/cars/{id}**: Retorna um carro específico do usuário autenticado.
- ** PUT <ins>/api/cars/{id}**: Atualiza as informações de um carro.
    - **Body**:
        ```json
        {
        "year": 2018,
        "licensePlate": "PDV-5415",
        "model": "CRETA",
        "color": "BLACK"
        }
        ```
- ** DELETE <ins>/api/cars/{id}**: Deleta um carro.


## 🛠 Solução Técnica
Solução técnica implementada para o projeto Desafio Pitang:

1. **Tecnologias**
* **Java 17** - Aproveitar as melhorias de desempenho
* **Spring Boot 3** - Otimizar o processo de configuração e desenvolvimento da aplicação
* **Angular 18** - Alta performace e escabilidade do projeto. Utilizei o PrimeNG para utilizar os componentes existentes
* **JUnit e Mockito** - Para garantir que as funcionalidades da aplicação estejam funcionando conforme esperado


2. **Arquitetura em camadas**
>A ideia é separar as responsabilidades da aplicação em camadas, permitindo maior organização, escalabilidade e manutenção do código.

* **Controllers** - Gerencia a interação entre o cliente (front-end) e o back-end por requisições HTTP
* **DTO** - Objetos que transportam dados entre as camadas da aplicação
* **Exceptions** - Gerencia erros que ocorrem durante o processamento da aplicação
* **CONFIG** - Fornece serviços de infraestrutura como a segurança da API
* **Model** - Contém as entidades que são mapeadas para o banco de dados
* **Repository** - Responsável pela comunicação com o banco de dados
* **Service** - Camada onde a regra de negócio é implementada

3**Autenticação Segura com JWT**
* O sistema utiliza Spring Security para proteção dos endpoints.
* Cada requisição autenticada deve incluir um token JWT no header: