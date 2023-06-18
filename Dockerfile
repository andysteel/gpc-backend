FROM maven:3.8.6-eclipse-temurin-17-alpine
WORKDIR /app
RUN ln -sf /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime \
&& mkdir -p /logs
EXPOSE 8183
COPY . .
RUN mvn clean install
ENTRYPOINT ["java","-jar","target/gpc.jar"]