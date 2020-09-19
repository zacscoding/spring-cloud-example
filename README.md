# Learning Spring Cloud :)  
Spring cloud 학습을 위한 저장공간!!! :)

## Spring boot with gradle + Docker boilerplate
[README.md](./templates/gradle-template)  
- gradle 기반의 Spring boot + cloud 멀티 모듈
- Dockerfile과 docker-compose를 이용하여 컨테이너화

<br />  

---  

## Spring boot tests  
[README.md](./tests)    

- RESTFul Spring boot test example
- Spring cloud contract(TBD)

---  

## Spring cloud configuration
[README.md](./configuration)  
Spring cloud config-server 관련 예제
- Spring cloud config server
  - repository:git,jdbc / event: rabbitmq
- Spring cloud config client
  - actuator/custom endpoint/ rabbitmq를 이용한 refresh 호출   

<br />  

---  

## Spring cloud discovery  
[README.md](./service-discovery)  
Spring cloud service discovery 관련 예제
- Spring cloud netflix eureka server/client
  - eureka 개요 및 분석
  - eureka 예제

<br />  

---  

## Spring cloud loadbalancer  
[README.md](./loadbalancer)  
Spring cloud service discovery + client loadbalancer 관련 예제  

- service discovery: eureka
  - loadbalancer: ribbon
