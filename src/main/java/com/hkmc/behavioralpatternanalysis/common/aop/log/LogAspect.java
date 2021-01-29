//package com.hkmc.behavioralpatternanalysis.common.aop.log;
//
//import java.util.Enumeration;
//import java.util.Optional;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import lombok.extern.slf4j.Slf4j;
//
///***************************************************
// * 설     명 : Spring AOP를 사용하여 공통 logging 구현
// ***************************************************/
//
//@Aspect
//@Slf4j
//public class LogAspect {
//
//    private String requestHeaderToString(HttpServletRequest request) {
//        Enumeration<String> headerNames = request.getHeaderNames();
//        StringBuilder headerText = new StringBuilder();
//
//        while (headerNames.hasMoreElements()) {
//            String key = (String) headerNames.nextElement();
//            headerText
//            .append(",\"")
//            .append(key)
//            .append("\"=\"")
//            .append(request.getHeader(key))
//            .append("\"");
//        }
//
//        return headerText.toString().substring(1);
//    }
//
//    @Before("execution(* com.hkmc.example..web.*Controller.*(..))")
//    public void requestLog(JoinPoint joinPoint) {
//        long threadId = Thread.currentThread().getId();
//
//        HttpServletRequest request =
//                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//
//        log.info("\n [=== REQUEST] \n [THREAD-ID] {} \n [HEADER] {} \n [{}] {} \n [PARAMS] {} \n",
//                threadId, requestHeaderToString(request), request.getMethod(), request.getRequestURI(),
//                joinPoint.getArgs());
//    }
//
//    @AfterReturning(value = "execution(* com.hkmc.example..web.*Controller.*(..))", returning = "response")
//    public void responseLog(Object response) {
//        response = Optional.ofNullable(response).orElse(new Object());
//
//        if (response.getClass().equals(ResponseEntity.class)) {
//            @SuppressWarnings("rawtypes")
//			ResponseEntity responseEntity = (ResponseEntity) response;
//
//            Object body = Optional.ofNullable(responseEntity.getBody()).orElse("");
//
//            log.info("\n [=== RESPONSE] \n [THREAD-ID] {} \n [CODE] {} \n [DATA] {} \n",
//                    Thread.currentThread().getId(), responseEntity.getStatusCode(), body.toString());
//        } else {
//            log.info("\n [=== RESPONSE] \n [THREAD-ID] {} \n [DATA] {} \n",
//                    Thread.currentThread().getId(), response.toString());
//        }
//    }
//
//    @AfterThrowing(value = "execution(* com.hkmc.example..web.*Controller.*(..))", throwing = "exception")
//    public void writeFailLog(JoinPoint joinPoint, Exception exception) throws RuntimeException {
//        log.info("\n [=== RESPONSE] \n [THREAD-ID] {} \n [ERROR] {} \n",
//                Thread.currentThread().getId(), exception.toString());
//    }
//
//}
