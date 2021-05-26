FROM  adoptopenjdk/openjdk8:jre8u292-b10-alpine
COPY red-lab.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
