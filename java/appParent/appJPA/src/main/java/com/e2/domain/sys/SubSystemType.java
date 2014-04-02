package com.e2.domain.sys;

public enum SubSystemType implements ISubSystemType {
    CORE{
    	@Override
    	public void accept(boolean flag) {
    		super.accept(flag);
    		System.out.println("---- CORE-----");
    	}    	
    },
    
    SWXEVD{
    	@Override
    	public void accept(boolean flag) {
    		super.accept(flag);
    		System.out.println("---- SWXEVD-----");
    	}    	
    },
    AVAYACMS,
    CISCO,
    CISCOUCCE,
    DEFINITY,
    GENERICRTA,
    SYMPOSIUM,
    XMLCONNECTOR,
    SWXIFACE,
    SMARTSYNC,
    WEB_SWS,
    DEPRECATED;
    
    private SubSystemType()
    {
        
    }

	@Override
	public void accept(boolean flag) {
		System.out.println("---- parent-----");
	}
}
