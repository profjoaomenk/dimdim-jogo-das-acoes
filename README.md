# 🎮 DimDim Stock Game

Um mini jogo educacional de mercado de ações desenvolvido com Java 17 Spring Boot, onde o usuário pode investir em empresas fictícias e testar sua sorte no mercado!

![DimDim Logo](src/main/resources/static/images/logo-dimdim.jpg)

[![Java](https://img.shields.io/badge/Java-21-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?logo=spring)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue?logo=apache-maven)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-Educational-yellow)](LICENSE)

## 🏢 Empresas Disponíveis

- 🏭 **Empresas ACME** - Produtos inovadores desde 1949
- ⚡ **Indústrias Stark** - Tecnologia de ponta e defesa
- 🦇 **Wayne Enterprises** - Conglomerado de Gotham City
- 🍫 **Wonka Industries** - Doces e chocolate premium
- ☂️ **Umbrella Corporation** - Farmacêutica e biotecnologia

## 🎯 Como Jogar

1. **Comece com 100 ações** na sua carteira
2. **Escolha uma empresa** para investir
3. **Aposte de 1 até 100 ações** (ou o máximo que você possui)
4. O mercado pode ter 3 resultados:
   - 📈 **Alta**: Ganhe metade das ações apostadas
   - 📉 **Baixa**: Perca metade das ações apostadas
   - 📊 **Estável**: Suas ações permanecem iguais

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Thymeleaf** (Template Engine)
- **Maven** (Gerenciador de dependências)
- **HTML5 + CSS3 + JavaScript**
- **Docker** (Containerização)

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Docker (opcional, para containerização)

## 🔧 Como Executar Localmente

### 1. Clone o repositório

git clone https://github.com/SEU-USUARIO/dimdim-stock-game.git
cd dimdim-stock-game


### 2. Compile o projeto

mvn clean package


### 3. Execute a aplicação

**Opção 1: Usando Maven**

mvn spring-boot:run


**Opção 2: Usando JAR**

java -jar target/dimdim-stock-game.jar


### 4. Acesse no navegador

Abra seu navegador e acesse: `http://localhost:8080`

## 🐳 Executar com Docker

### 1. Construir a imagem

docker build -t dimdim-stock-game:latest .


### 2. Executar o container

docker run -d --name dimdim-stock-game -p 8080:8080 dimdim-stock-game:latest


### 3. Verificar logs


docker logs -f dimdim-stock-game


### 4. Parar e remover o container

docker stop dimdim-stock-game
docker rm dimdim-stock-game


## ☁️ Deploy no Azure Web App

O projeto inclui um arquivo `azure-pipelines.yml` para CI/CD automatizado no Azure DevOps.

### Pré-requisitos:
1. Azure Container Registry (ACR)
2. Service Connection configurada no Azure DevOps
3. Repositório no Azure Repos ou GitHub

### Variáveis necessárias:
- `azureSubscription`: Service connection do Azure
- `dockerRegistryServiceConnection`: Service connection do ACR
- `webAppName`: Nome único do Web App
- `containerRegistry`: URL do ACR (ex: dimdimacr.azurecr.io)

## 📁 Estrutura do Projeto

dimdim-stock-game/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/dimdim/stockgame/
│ │ │ ├── StockGameApplication.java
│ │ │ ├── controller/
│ │ │ │ ├── StockGameController.java
│ │ │ │ └── StockGameRestController.java
│ │ │ ├── service/
│ │ │ │ └── StockGameService.java
│ │ │ └── model/
│ │ │ ├── Company.java
│ │ │ ├── StockTransaction.java
│ │ │ ├── GameResult.java
│ │ │ └── PlayerWallet.java
│ │ └── resources/
│ │ ├── application.properties
│ │ ├── static/
│ │ │ ├── css/style.css
│ │ │ ├── js/script.js
│ │ │ └── images/logo-dimdim.jpg
│ │ └── templates/
│ │ └── index.html
│ └── test/
├── target/
├── Dockerfile
├── azure-pipelines.yml
├── pom.xml
├── .gitignore
└── README.md


## 🎨 Cores do Projeto

- **Amarelo Principal**: #FFD700
- **Amarelo Claro**: #FFF700
- **Azul DimDim**: #1E3A8A
- **Branco**: #FFFFFF

## 📊 Funcionalidades

- ✅ Sistema de carteira com 100 ações iniciais
- ✅ Limitação dinâmica de apostas baseada no saldo
- ✅ Estatísticas de jogadas, vitórias e derrotas
- ✅ Game Over quando ações chegam a zero
- ✅ Botão de reset para reiniciar o jogo
- ✅ Design responsivo para mobile e desktop
- ✅ Animações e feedback visual
- ✅ API REST para integração

## 🔌 API REST

### Endpoints disponíveis:

**GET** `/api/companies` - Lista todas as empresas

[
{
"id": "acme",
"name": "Empresas ACME",
"icon": "🏭",
"basePrice": 100.0,
"description": "Produtos inovadores desde 1949"
}
]


**GET** `/api/wallet` - Retorna dados da carteira

{
"totalShares": 100,
"gamesPlayed": 0,
"gamesWon": 0,
"gamesLost": 0
}


**POST** `/api/play` - Realizar uma jogada

{
"companyId": "acme",
"quantity": 10
}


**POST** `/api/reset` - Resetar a carteira

## 📝 Licença

Este é um projeto educacional desenvolvido para fins de aprendizado.

## 👨‍💻 Autor

Desenvolvido para demonstrar conceitos de:
- Desenvolvimento web com Spring Boot
- Containerização com Docker
- CI/CD com Azure DevOps
- Design responsivo
- APIs RESTful

---

⭐ **DimDim** - Prof João Menk | Este é um jogo educacional. Investimentos reais envolvem riscos


