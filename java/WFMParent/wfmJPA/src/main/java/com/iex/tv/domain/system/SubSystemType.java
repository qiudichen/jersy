package com.iex.tv.domain.system;

public enum SubSystemType implements ISubSystemType {
	CORE,
    SWXEVD,
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
    DEPRECATED{
    	@Override
    	public boolean isServer() {
    		return false;
    	}    	
    };

    private SubSystemType() {
        
    }

	@Override
	public boolean isServer() {
		return true;
	}
}
