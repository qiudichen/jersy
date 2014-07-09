package com.iex.tv.core.config;


public interface ICustomerContextHolder extends ICustomerContextResolver {

    public void switchIn(Long customerId) throws ConfigException;

    public void switchOut();
    
    public Long getCustomerId();
}
