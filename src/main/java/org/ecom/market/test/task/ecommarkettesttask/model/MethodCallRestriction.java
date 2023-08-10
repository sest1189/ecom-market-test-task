package org.ecom.market.test.task.ecommarkettesttask.model;

public class MethodCallRestriction {
    private int callRestrictionNumber;
    private int timePeriod;
    private String methodName;

    public MethodCallRestriction(int callRestrictionNumber, int timePeriod) {
        this.callRestrictionNumber = callRestrictionNumber;
        this.timePeriod = timePeriod;
    }

    public MethodCallRestriction(int callRestrictionNumber, int timePeriod, String methodName) {
        this.callRestrictionNumber = callRestrictionNumber;
        this.timePeriod = timePeriod;
        this.methodName = methodName;
    }

    public int getCallRestrictionNumber() {
        return callRestrictionNumber;
    }

    public void setCallRestrictionNumber(int callRestrictionNumber) {
        this.callRestrictionNumber = callRestrictionNumber;
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
