FROM gradle:7.3-jdk11-alpine
RUN mkdir -p /static/images
COPY  . /home/gradle/src
COPY ./src/main/resources/static/* /static
WORKDIR /home/gradle/src
EXPOSE 8080

ENTRYPOINT ["gradle", "bootRun"]