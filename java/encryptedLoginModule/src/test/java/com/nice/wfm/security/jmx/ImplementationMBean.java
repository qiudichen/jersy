package com.nice.wfm.security.jmx;

public interface ImplementationMBean {

    public void setName(String name);
    public String getName();

    public void setNumber(int number);
    public int getNumber();
    public boolean getKilled();
    public void setKilled(boolean killed);
}