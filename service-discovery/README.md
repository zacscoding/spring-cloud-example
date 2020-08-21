# Service discovery with Eureka :)  

- 유레카 시작하기
    - 서버 : 디펜던시, 설정, 코드, web-ui
    - 클라이언트 :  


demo 포트 구성

- eureka server : 3000
  - http://localhost:3000/eureka-ui
- account server : 3100, 3101
- article server : 3200    

```cmd
// eureka server
$ tools/script/start.sh server default 3000

// account server (2)
$ tools/script/start.sh account default 3100
$ tools/script/start.sh account default 3101

// article server(1)
$ tools/script/start.sh article default 3200
```




----  

## References  

- https://docs.spring.io/spring-cloud-netflix/docs/2.2.4.RELEASE/reference/html/
- https://medium.com/swlh/spring-cloud-high-availability-for-eureka-b5b7abcefb32  
- https://github.com/Netflix/eureka/wiki/Understanding-Eureka-Peer-to-Peer-Communication