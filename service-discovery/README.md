# Service discovery  

# 목차  

- [Overview](#Overview)  
- [DiscoveryClient](#DiscoveryClient)
- [Netflix Eureka](#Netflix-Eureka)
- [References](#References)

---  

# Overview  

- 클라우드 네이티브 시스템은 동적이다.(필요에 따라 그때그때 올라가고, 내려가기도 함)  
- 어떤 서비스가 내려가고 올라가더라도 그 서비스를 이용하는 다른 서비스는 영향을 받으면 안된다.
- 해당 서비스를 아래와 같이 설정하면 클라이언트 -> 서버를 고정적으로 결합하므로 동적인 클라우드 네이티브 시스템에는 적합하지 않다.  
```yaml
member-service:
    endpoints:
      - http://172.12.35.65:8080/members
      - http://172.12.35.67:8080/members
```  
- DNS를 이용할 수도 있지만, 클라우드 환경에서는 가장 좋은 방법이라고 볼 수 없다.
  - 클라이언트 내부에 캐시를 둘 수 있다는 것이 장점인데, 사용되지 않는 IP 주소를 캐시하고 있을 수 있다.
  - TTL(time to live) 값을 짧게 잡으면 DNS 레코드를 더 자주 받아오는데 많은 시간을 쓰게 된다.
  - DNS 로드밸런싱을 적용하면 스티키 세션(sticky session)은 지원하지만 그 이상의 기능이 필요하면 감당하기 어렵다.(예를들어 OAuth2나 JWT의
  access token 기반 밸런싱)
  - 프록시 역할을 하는 가상 로드밸런서는 시스템의 상태나 요청 처리에 필요한 작업량을 알지 못한다.
    - 초기 연결을 서비스에 분산할 뿐, 연결에 포함된 요청 처리에 필요한 작업량을 분산하지는 못한다.
- 인프라가 아니라 프로그래밍을 통해 더 나은 라우팅 전략을 구현할 수 있다.(*작업량은 어떻게 분산..?*)

---  

# DiscoveryClient   

- 서비스 레지스트리는 서비스 인스턴스와 서비스가 제공하는 API를 내용으로 하는 테이블이다.  
  - 서비스 레지스트리는 CAP(Consistency 일관성, Availability 가용성, Partition tolerance 분리 내구성) 정리의 제약도 받는다.
- 스프링 클라우드는 DiscoveryClient 추상화를 통해 클라이언트가 다양한 유형의 서비스 레지스트리를 쉽게 이용할 수 있게 해준다.
  - 다양한 구현체들을 Plugin 하여 사용할 수 있다.
  - 클라우드 파운드리, 아파치 주키퍼, 해시코프 컨설, 넷플릭스 유레카, CoreOS etcd
- 일부 서비스 레지스트리는 클라이언트도 레지스트리에 등록해야 한다.  
  - 많은 DiscoveryClient 추상화 구현체는 애플리케이션 기동 시 자동으로 등록해준다.

<br /> 

> DiscoveryClient.java 

```java
package org.springframework.cloud.client.discovery;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.Ordered;

public interface DiscoveryClient extends Ordered {

	/**
	 * Default order of the discovery client.
	 */
	int DEFAULT_ORDER = 0;

	/**
	 * A human-readable description of the implementation, used in HealthIndicator.
	 * @return The description.
	 */
	String description();

	/**
	 * Gets all ServiceInstances associated with a particular serviceId.
	 * @param serviceId The serviceId to query.
	 * @return A List of ServiceInstance.
	 */
	List<ServiceInstance> getInstances(String serviceId);

	/**
	 * @return All known service IDs.
	 */
	List<String> getServices();

	/**
	 * Default implementation for getting order of discovery clients.
	 * @return order
	 */
	@Override
	default int getOrder() {
		return DEFAULT_ORDER;
	}
}
```  

<br /> 

간단하게 `org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient` 살펴보면 `com.netflix.discovery.EurekaClient`를 이용하여  
Spring cloud의 `ServiceInstance`로 컨버팅 해주고 있다.  

```java
package org.springframework.cloud.netflix.eureka;

...
public class EurekaDiscoveryClient implements DiscoveryClient {
    ...
	@Override
	public List<ServiceInstance> getInstances(String serviceId) {
		List<InstanceInfo> infos = this.eurekaClient.getInstancesByVipAddress(serviceId,
				false);
		List<ServiceInstance> instances = new ArrayList<>();
		for (InstanceInfo info : infos) {
			instances.add(new EurekaServiceInstance(info));
		}
		return instances;
	}

	@Override
	public List<String> getServices() {
		Applications applications = this.eurekaClient.getApplications();
		if (applications == null) {
			return Collections.emptyList();
		}
		List<Application> registered = applications.getRegisteredApplications();
		List<String> names = new ArrayList<>();
		for (Application app : registered) {
			if (app.getInstances().isEmpty()) {
				continue;
			}
			names.add(app.getName().toLowerCase());

		}
		return names;
	}
    ...
}
```  

---  

# Netflix Eureka  

;TBD  

---  

# References  

- https://docs.spring.io/spring-cloud-netflix/docs/2.2.4.RELEASE/reference/html/
- https://medium.com/swlh/spring-cloud-high-availability-for-eureka-b5b7abcefb32  
- https://github.com/Netflix/eureka/wiki/Understanding-Eureka-Peer-to-Peer-Communication