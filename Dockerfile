FROM openjdk:17
EXPOSE 9090

ADD target/edu-manage.jar edu-manage.jar
ENTRYPOINT ["java","-jar","/edu-manage.jar"]


