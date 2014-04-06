package com.iex.tv.domain.system;

public enum SubSystemType implements ISubSystemType {
    CORE,
    SWXEVD,
    SWXIFACE,
    SMARTSYNC,
    SERVICES,           //Configurations specific to service nodes
    WEBSTATION,
    PM,                 //Performance manager items
    IMPORTER,
    EXPORTER, 
    WORKMGR, 
    WORKSTATION,        //Items related to the RCP Client
    SERVER_DEFAULT,     //Defaults for the server based nodes - things like DB Configs
    UNIVERSAL,          //For the entire TV4 System - including server and clients
    MEMORY,
    COACHING,           //Coaching package config values
    UNKNOWN,
    LEGACY,
    WEB_AWS,            //Allow AWS and SWS specific properties
    WEB_SWS,
    DEPRECATED;
    
    private SubSystemType() {
        
    }

	@Override
	public boolean isServer() {
		return true;
	}
	
	public static SubSystemType fromStringName(String nameVal) {
		SubSystemType type = null;
		if (nameVal != null) {
			for (SubSystemType t : SubSystemType.values()) {
				if (nameVal.equals(t.toString())) {
					return t;
				}
			}
		}
		return type;
	}
}
