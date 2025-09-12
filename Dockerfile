# Etapa de build com Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia os arquivos do projeto para o container
COPY . .

# Compila e empacota a aplicação sem rodar os testes
RUN mvn clean package -DskipTests

# Etapa de runtime com Java 21
FROM eclipse-temurin:21-jre-jammy

# Cria um usuário não-root para rodar a aplicação
RUN useradd -m appuser

WORKDIR /home/appuser

# Copia o .jar gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

# Define o usuário não-root
USER appuser

# Exponha a porta que sua aplicação utiliza
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]