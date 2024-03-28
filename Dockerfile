FROM openjdk:8

# COPY: 将应用的配置文件也拷贝到镜像中。
COPY ./target/shop-1.0-SNAPSHOT.jar /app.jar

CMD ["--server.port=9601"]
# EXPOSE：声明端口
EXPOSE 9601

ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=pro","--spring.application.key=92644664"]
# ENTRYPOINT：docker启动时，运行的命令，这里容器启动时直接运行jar服务。