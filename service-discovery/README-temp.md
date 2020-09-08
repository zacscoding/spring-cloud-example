## Eureka Server HA Tests

### Test1-1)  

- Server1 : standalone mode (register: false, fetch: false)
- Server2 : cluster with server1 (register: true, fetch: false)
- AccountService : connect to Server1
- Result : Server1 Registry[Server2, AccountService], Server2 Registry : []  

> Server1(standalone)  

```
eureka:
  client:
    registerWithEureka: false
    fetchRegistry: false
```

> Server2(cluster)

```
eureka:
  instance:
    hostname: peer-1-server.com
  client:
    registerWithEureka: true
    fetchRegistry: false
    serviceUrl:
      # Server1 endpoint   
      defaultZone: http://localhost:3000/eureka/
```  

> Result

- Server1 Registry : [Server2]
- Server2 Registry : []

> Accoun Service

```
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:3000/eureka/
```  

> Result  

- Server1 Registry : [Server2, AccountService1]  
- Server2 Registry : []  

---  

### Test1-2)  

- Server1 : standalone mode (register: false, fetch: false)
- Server2 : cluster with server1 (register: true, fetch: true)
- AccountService : connect to Server1
- Result : Server1 Registry[Server2, AccountService], Server Registry[Server2, AccountService] OR [Server2] OR []

