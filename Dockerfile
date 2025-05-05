FROM openjdk:21
COPY ./build/libs/payment-service.jar payment-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "-jar", "payment-service.jar"]
