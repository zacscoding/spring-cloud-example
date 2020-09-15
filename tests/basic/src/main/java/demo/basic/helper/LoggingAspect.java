package demo.basic.helper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
//@Aspect
public class LoggingAspect {

    @Around("execution(* javax.servlet.http.HttpServlet.service(..))")
    public Object loggingServletService(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        logger.info("Servlet::service() stack trace\n{}", getStackTrace());
        return result;
    }

    private String getStackTrace() {
        final StringBuilder sb = new StringBuilder();
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (int i = 2; i < stackTrace.length; i++) {
            sb.append(stackTrace[i].toString()).append('\n');
        }

        return sb.toString();
    }
}
