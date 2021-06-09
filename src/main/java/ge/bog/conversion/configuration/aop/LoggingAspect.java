package ge.bog.conversion.configuration.aop;

import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
    /**
     * @param joinPoint
     * trigger of any request on Controller package
     * log request info
     */
    @SneakyThrows
    @Before("execution(* ge.bog.conversion.controller.*.*(..))")
    public void preLogger(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String args = Arrays.toString(joinPoint.getArgs());

        if (request != null) {
            LOGGER.info("=============Received Http Request============");
            LOGGER.info("Http Method = {}", request.getMethod());
            LOGGER.info("URI = {}", request.getRequestURI());
            LOGGER.info("CLASS Method = {}", joinPoint.getSignature().getDeclaringType() + "." + joinPoint.getSignature().getName());
            LOGGER.info("ARGS = {}", args);
            LOGGER.info("REQUESTER IP = {}", request.getRemoteAddr());
        }
    }
}