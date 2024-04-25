FROM openjdk:8-jdk-alpine
# 匿名数据卷，如果运行时没有挂载数据卷，会将数据挂载到此目录
VOLUME /docker_data/tmp/
ADD order-0.0.1-SNAPSHOT.jar order-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/order-backend.jar"]
MAINTAINER JesseChen