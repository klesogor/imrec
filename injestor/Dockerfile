FROM gradle:7.3-jdk11-alpine
COPY  . /home/gradle/src
WORKDIR /home/gradle/src
RUN mkdir /imrec-images
EXPOSE 8080

ENTRYPOINT ["gradle", "bootRun"]