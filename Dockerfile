FROM openjdk:14-jdk-alpine
EXPOSE 8080
ADD target/*.jar otomoto.jar
ENTRYPOINT ["java","-jar","/otomoto.jar"]
