# Projeto Nome-do-Projeto

### **Repositório do Backend**
1. Clone o repositório:
   ```bash
   git clone https://github.com/Rodrigofds/desafio-tcepe.git

### **Repositório do Frontend**
1. Clone o repositório:
   ```bash
   git clone https://github.com/Rodrigofds/fed-desafio-tcepe.git

---
## **Como Executar o Projeto**

 **Execução do Backend**

    mvn spring-boot:run

    O backend estará disponível em http://localhost:8080.

 **Execução do Front**

    1. npm install

    2. ng serve

    O frontend estará disponível em http://localhost:4200.

---

## **Sobre o Projeto**
Este projeto é um desafio técnico que aborda a criação de uma API Rest de gestão de usuários e veículos,
com foco na utilização de práticas modernas de desenvolvimento e estruturação de código.


## **Estórias de Usuário**
1. **[USER001] Listar Usuários** - Como usuário, quero listar os usuários da API.
2. **[USER002] Cadastrar um novo Usuário** - Como usuário, quero cadastrar um novo usuário na API.
3. **[USER003] Buscar um Usuário pelo id** - Como usuário, quero buscar um usuário pelo id na API.
4. **[USER004] Remover um Usuário pelo id** - Como usuário, quero remover um usuário pelo id na API.
5. **[USER005] Atualizar Usuário pelo id** - Como usuário, quero atualizar um usuário pelo id na API.
6. **[USER006] Listar Carros** - Como usuário, quero obter a lista de todos os carros do usuário logado.
7. **[USER007] Autenticar Usuário na API** - Como um usuário, quero me autenticar com login e password na API e retornar um token de acesso da API (JWT) com as informações do usuário logado.
8. **[USER008] Retornar as informações do usuário logado** - Como usuário, quero retornar as informações do usuário logado.
9. **[USER009] Buscar um Carro do Usuário logado pelo id** - Como usuário, quero buscar um carro do usuário logado pelo id.
10. **[USER010] Cadastrar um novo Carro** - Como usuário, quero cadastrar um novo carro para o usuário logado.
11. **[USER011] Atualizar um Carro do Usuário logado pelo id** - Como usuário, quero atualizar um carro do usuário logado pelo id.
12. **[USER012] Remover um Carro do Usuário logado pelo id** - Como usuário, quero remover um carro do usuário logado pelo id.

---

## **Solução**
A solução implementada atende aos requisitos do desafio e utiliza as seguintes abordagens:

- **Backend**: Desenvolvido com Spring Boot e Java 11, utilizando JUnit 5 e Mockito para testes unitários. O Maven foi utilizado para o gerenciamento de dependências.
- 
- **Frontend**: Desenvolvido com Angular (v16.2.12) para criar uma interface rica e responsiva.
- 
- **Autenticação**: Implementada utilizando JWT para garantir a segurança das operações.
- 
- **Testes de API**: A coleção de testes está disponível no arquivo: `Desafio Pitang TCE-PE.postman_collection.json`.

---

## **Tecnologias Utilizadas**
- **Backend**: Spring Boot, Java 11, JUnit 5, Mockito, Maven.
- **Frontend**: Angular 16.2.12.
- **Testes de API**: Postman.

---
  

### **Pré-requisitos**
Certifique-se de ter os seguintes itens instalados:
- [Node.js](https://nodejs.org/) (versão 16 ou superior)
- [Angular CLI](https://angular.io/cli) (versão mais recente)
- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Apache Maven](https://maven.apache.org/)
- Banco de dados H2 (embutido no projeto)

