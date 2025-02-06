FROM openjdk:21
COPY ./build/libs/paymentService.jar paymentService.jar
ENTRYPOINT ["java", "-jar", "paymentService.jar"]
