package com.nice.wfm.security.jmx;

public class Implementation implements ImplementationMBean {
	private String name ;
    private int number;
    private boolean killed = false;

    public Implementation(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public void setName(String name) {
        this.name = name;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public boolean getKilled() {
        return killed;
    }

    @Override
    public void setKilled(boolean killed) {
        this.killed = killed;

    }
}
