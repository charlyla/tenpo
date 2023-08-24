FROM openjdk:17-jdk-slim
COPY target/tenpo-*.jar tenpo.jar
EXPOSE 8070
CMD ["java", "-jar", "tenpo.jar"]
