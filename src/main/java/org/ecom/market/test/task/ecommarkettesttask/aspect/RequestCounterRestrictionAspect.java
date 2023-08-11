package org.ecom.market.test.task.ecommarkettesttask.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.ecom.market.test.task.ecommarkettesttask.annotations.AspectCallRestriction;
import org.ecom.market.test.task.ecommarkettesttask.model.MethodCallRestriction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@AspectCallRestriction
@Aspect
@Component
public class RequestCounterRestrictionAspect {

    public Map<String, MethodCallRestriction> methodRestrictions = new HashMap<>();
    public volatile Map<String, Map<String, List<Date>>> numberOfCalls = new ConcurrentHashMap<>();


    @Around("@annotation(org.ecom.market.test.task.ecommarkettesttask.annotations.RequestCounterRestriction)")
    public Object callRestrictionChecker(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getName();
        methodName = className + "." + methodName;
        MethodCallRestriction methodCallRestriction = methodRestrictions.get(methodName);

        for (Object o : joinPoint.getArgs()) {
            if (o instanceof HttpServletRequest) {
                String ipAddress = ((HttpServletRequest) o).getRemoteHost();
                log.info("ip address: " + ipAddress);
                return validateNewCallAvailability(joinPoint, methodName, methodCallRestriction, ipAddress);
            }
        }

        return validateNewCallAvailability(joinPoint, methodName, methodCallRestriction);
    }

    private Object validateNewCallAvailability(ProceedingJoinPoint joinPoint,
                                               String methodName,
                                               MethodCallRestriction methodCallRestriction) throws Throwable {
        return validateNewCallAvailability(joinPoint, methodName, methodCallRestriction, methodName);
    }

    private Object validateNewCallAvailability(ProceedingJoinPoint joinPoint,
                                                            String methodName,
                                                            MethodCallRestriction methodCallRestriction,
                                                            String ipAddress) throws Throwable {
        Map<String, List<Date>> methodCallsMap = numberOfCalls.get(methodName);
        Date newCall = new Date();
        boolean isCallProcessingAvailable;
        if (methodCallsMap == null) {
            isCallProcessingAvailable = true;
            methodCallsMap = new ConcurrentHashMap<>();
            List<Date> calls = new ArrayList<>();
            calls.add(newCall);
            methodCallsMap.put(ipAddress, calls);
            numberOfCalls.put(methodName, methodCallsMap);
        } else if (methodCallsMap.get(ipAddress) == null) {
            isCallProcessingAvailable = true;
            List<Date> calls = new ArrayList<>();
            calls.add(newCall);
            methodCallsMap.put(ipAddress, calls);
        } else {
            isCallProcessingAvailable = isNewCallAvailable(methodCallsMap.get(ipAddress), methodCallRestriction, newCall);
            methodCallsMap.get(ipAddress).add(newCall);
        }

        if (!isCallProcessingAvailable) {
            return new ResponseEntity(HttpStatus.BAD_GATEWAY);
        } else {
            return joinPoint.proceed();
        }
    }

    private boolean isNewCallAvailable(List<Date> calls, MethodCallRestriction methodCallRestriction, Date callDateAndTime) {
        int callRestrictionNumber = methodCallRestriction.getCallRestrictionNumber();
        if (calls == null || calls.size() < callRestrictionNumber) {
            return true;
        } else {
            long timeOfNewCall = callDateAndTime.getTime();
            long latestTimeCallByRestrictionLimit =
                    calls.get(calls.size() - (methodCallRestriction.getCallRestrictionNumber())).getTime();
            return methodCallRestriction.getTimePeriod() <= timeOfNewCall - latestTimeCallByRestrictionLimit;
        }
    }
}
