FROM hub.c.163.com/library/java:8-jdk
MAINTAINER pengkangzaia@foxmail.com
VOLUME /tmp
ADD target/tablesync-0.0.1-SNAPSHOT.jar tablesync.jar
ENTRYPOINT ["java", "-jar", "/tablesync.jar"]