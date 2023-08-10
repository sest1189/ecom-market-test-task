package org.ecom.market.test.task.ecommarkettesttask.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestCounterRestriction {
    String requestCount();

    String limitationTimePeriod();
}
