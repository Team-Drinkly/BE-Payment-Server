FROM openjdk:21

# JAR 복사
COPY ./build/libs/payment-service.jar payment-service.jar

# 기본 ENV 설정 (선택적, 기본값: prod)
ENV SPRING_PROFILE=prod
ENV TZ=Asia/Seoul

# ENTRYPOINT: 실행 시점에 외부 환경변수 사용
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${SPRING_PROFILE} -Duser.timezone=${TZ} -jar payment-service.jar"]
