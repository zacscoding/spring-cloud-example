package article;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LoggerAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // @Around("execution(* org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient.execute(..))")
    @Around("execution(* org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient.choose(..))")
    public Object executeOnBefore(ProceedingJoinPoint pjp) throws Throwable {
        logger.warn("## {} => arguments : {}", pjp.getSignature(), pjp.getArgs().length);
        return pjp.proceed();

//        try {
//            final String serviceId = (String) pjp.getArgs()[0];
//            final ServiceInstance serviceInstance = (ServiceInstance) ret;
//            logger.warn("BlockingLoadBalancerClient::execute(). serviceId: {} --> {}",
//                        serviceId, objectMapper.writeValueAsString(serviceInstance));
//        } catch (Exception e) {
//            logger.warn("Failed to BlockingLoadBalancerClient::execute() before hook");
//        }
//        return ret;
    }
}
