# SprintJavaPz - Gestão de Pátios Mottu com IoT

## Descrição do Projeto
Este projeto propõe uma solução inteligente para a gestão de pátios da Mottu, empresa que enfrenta dificuldades em rastrear e organizar suas motos manualmente. A proposta agora é utilizar **dispositivos IoT** para identificar automaticamente a localização das motos e a ala em que elas estão, enviando sinais para a API do sistema em tempo real.

## Problema
- Mais de 100 filiais com organização manual das motos.
- Verificação manual causa erros e falta de rastreabilidade.
- O sistema atual não escala com o crescimento da empresa.

## Solução
- Cada moto possui sensores IoT que informam sua **posição e ala** em tempo real.
- O backend recebe os sinais via API e atualiza o sistema com o status e localização da moto.
- Visualização e gerenciamento de motos e alas através de interface web (Spring Boot + Thymeleaf).
- Futuramente, integração com aplicativo móvel para monitoramento remoto.

## Funcionalidades Principais
- Cadastro, edição e exclusão de motos e alas.
- Vinculação de motos às alas.
- Listagem de motos com filtros por status, ala e modelo.
- Controle de usuários com permissões (ADMIN e padrão).
- Atualização automática da localização da moto via sinais IoT.
- Cache e paginação para otimização de performance.

## Integrantes do Grupo
- Gabrielly Macedo
- Rafael Macoto
- Fernando Aguiar

## Tecnologias Utilizadas
- Java
- Spring Boot
- Spring Security
- Thymeleaf
- Flyway
- Maven
- Docker

## Estrutura do Projeto
src/ – Código-fonte da aplicação Java
pom.xml – Configuração de dependências com Maven
Dockerfile – Para construção de imagem Docker
.mvn/ – Wrapper do Maven para execução sem instalação global

bash
Copiar código

## Pré-requisitos
- Java JDK 21 ou superior
- Maven 3.6+
- Docker (opcional)
- Git

## Como Executar

### Executando Localmente com Maven
```bash
git clone https://github.com/RafaMacoto/SprintJavaPz.git
cd SprintJavaPz
./mvnw spring-boot:run
Acesse no navegador:

arduino
Copiar código
http://localhost:8080
Executando com Docker
bash
Copiar código
docker build -t sprintjavapz .
docker run -p 8080:8080 sprintjavapz