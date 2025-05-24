
## Descrição do Projeto

Este projeto propõe uma solução inteligente para a gestão de pátios da Mottu, empresa que enfrenta dificuldades em rastrear e organizar suas motos manualmente. A proposta é utilizar **Visão Computacional** para automatizar a identificação, classificação e localização de motos em tempo real.

### Problema
- Mais de 100 filiais com organização manual das motos.
- Verificação visual causa erros e falta de rastreabilidade.
- O sistema atual não escala com o crescimento da empresa.

### Solução
- Uso de câmeras como sensores inteligentes para capturar imagens dos pátios.
- Aplicação de modelos de **IA com CNN** (Redes Neurais Convolucionais) para identificar o tipo de moto (Pop, Sport ou Elétrica).
- Exibição dos resultados em um **mapa digital interativo**, com status em tempo real.

> Futuramente, os dados processados serão integrados a um aplicativo para acesso e controle remoto.

## Integrantes do Grupo

- Gabrielly Macedo  
- Rafael Macoto  
- Fernando Aguiar

## Tecnologias Utilizadas

- Java  
- Spring Boot  
- Maven  
- Docker  


## Estrutura do Projeto

- `src/` – Código-fonte da aplicação Java  
- `pom.xml` – Configuração de dependências com Maven  
- `Dockerfile` – Para construção de imagem Docker  
- `.mvn/` – Wrapper do Maven para execução sem instalação global

## Pré-requisitos

- Java JDK 21 ou superior  
- Maven 3.6+  
- Docker (opcional)  
- Git

## Como Executar

### Executando Localmente com Maven

1. Clone o repositório:

```bash
git clone https://github.com/RafaMacoto/SprintJavaPz.git
cd SprintJavaPz
Compile e inicie a aplicação:


./mvnw spring-boot:run
Acesse no navegador:


http://localhost:8080
Executando com Docker
Construa a imagem:


docker build -t sprintjavapz .
Execute o container:


docker run -p 8080:8080 sprintjavapz
