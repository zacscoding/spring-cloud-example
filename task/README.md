# Spring boot with gradle template  
gradle 기반의 멀티 모듈을 구성하고 Docker container를 만들기 위한 템플릿 프로젝트.

- [Getting started](#Getting-started)  
- [Configure](#Configure)

---  

# Getting started

> ## 1. gradlew의 bootRun 이용하기  

<br />

```bash
// gradlew 를 이용한 account-service 실행
$ ./gradlew :account-service:bootRun

// 미리 작성한 스크립트로 account-service 실행
$ ./tools/script/start.sh account
```

<br />

> ## 2. Docker compose를 이용하기

<br />

```bash
// project build & docker containerize
// i.e (1) gradlew clean build (2) docker build
$ ./tools/script/compose.sh build


// start services
$ ./tools/script/compose.sh up
```  

<br />

---  

# Configure
;TBD  

<br />  

---  

# References

- https://spring.io/blog/2020/01/27/creating-docker-images-with-spring-boot-2-3-0-m1
