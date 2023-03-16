package com.uniware.integrations.client.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniware.integrations.client.dto.BaseRequest;
import com.uniware.integrations.client.dto.BaseResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class FlipkartAspect {

    @Autowired
    private ObjectMapper mapper;

    private static final Logger LOG  = LoggerFactory.getLogger(FlipkartAspect.class);

    @Around("execution(* com.uniware.integrations.flipkart.services.FlipkartSellerApiService.*(..)) || execution(* com.uniware.integrations.flipkart.services.FlipkartSellerPanelService.*(..))")
    public Object capture(final ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object[] ob = pjp.getArgs();
        for (Object i : ob) {
            if(!(i instanceof BaseRequest)){
                continue;
            }

            try {
                LOG.info("{} Request: {}", method.getName(), mapper.writeValueAsString(i).replaceAll("\n", ""));
            } catch (JsonProcessingException e) {
                LOG.info("{} Request: {}", method.getName(), i.toString());
            }
        }

        long startTime = System.currentTimeMillis();
        Object result = null;

        try {
            result = pjp.proceed();

            if (result instanceof BaseResponse) {
                try {
                    LOG.info("{} Response: {}", method.getName(), mapper.writeValueAsString(result).replaceAll("\n", ""));
                } catch (JsonProcessingException e) {
                    LOG.info("{} Response: {}", method.getName(), result.toString());
                }

                LOG.info("{} Response Code: {}", method.getName(), ((BaseResponse) result).getResponseStatus().name());
            }
        } catch (Exception ex) {
            LOG.error("After throwing : {}, {}", ex.getMessage(), ex.getCause());
            throw ex;
        } finally {
            long executionTime = (System.currentTimeMillis() - startTime);
            LOG.info("{} took {} ms to execute.", method.getName(), executionTime);
        }
        return result;

    }

}
