package org.ecom.market.test.task.ecommarkettesttask.annotations;

import lombok.extern.slf4j.Slf4j;
import org.ecom.market.test.task.ecommarkettesttask.config.SpringConfig;
import org.ecom.market.test.task.ecommarkettesttask.model.MethodCallRestriction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
public class RequestCounterRestrictionAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final Properties properties;
    private final Map<String, MethodCallRestriction> map = new HashMap<>();

    @Autowired
    public RequestCounterRestrictionAnnotationBeanPostProcessor(
            @Qualifier(value = SpringConfig.APP_PROPERTIES) Properties properties) {
        this.properties = properties;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        for (Method m : methods) {
            RequestCounterRestriction annotation = m.getAnnotation(RequestCounterRestriction.class);
            if (annotation != null) {
                String methodName = bean.getClass().getName() + "." + m.getName();
                int callRestriction = getValueForCallRestriction(annotation);
                int limitationTimePeriod = getValueForLimitationTimePeriod(annotation);

                map.put(methodName, new MethodCallRestriction(callRestriction, limitationTimePeriod, methodName));

            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        AspectCallRestriction callRestriction = bean.getClass().getAnnotation(AspectCallRestriction.class);
        if (callRestriction != null) {
            Field [] fields = bean.getClass().getFields();
            for (Field f : fields) {
                if (f.getName().equals("methodRestrictions")) {
                    f.setAccessible(true);
                    ReflectionUtils.setField(f, bean, map);
                }
            }
        }

        return bean;
    }

    private int getValueForCallRestriction(RequestCounterRestriction annotation) {
        if (annotation.requestCount().contains(".")) {
            return Integer.parseInt(properties.getProperty(annotation.requestCount()));
        } else {
            return Integer.parseInt(annotation.requestCount());
        }
    }

    private int getValueForLimitationTimePeriod(RequestCounterRestriction annotation) {
        if (annotation.limitationTimePeriod().contains(".")) {
            return Integer.parseInt(properties.getProperty(annotation.limitationTimePeriod())) * 1000;
        } else {
            return Integer.parseInt(annotation.limitationTimePeriod()) * 1000;
        }
    }
}
