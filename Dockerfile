FROM openjdk:14-jdk-alpine
EXPOSE 8080
ADD target/*.jar ottomoto.jar
ENTRYPOINT ["java","-jar","/ottomoto.jar"]
