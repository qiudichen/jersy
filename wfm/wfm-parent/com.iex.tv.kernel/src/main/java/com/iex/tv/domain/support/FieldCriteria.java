package com.iex.tv.domain.support;

public class FieldCriteria extends SortCriteria {
	public FieldCriteria.Key getKey() { return null;}
	
	public boolean isFiltered() {
		return false;
	}
	
	public String getFilter() { return null;}
	
	public static class Key {
		public boolean isAssociatedField() { return false;}
		public String getAssociation() { return null;}
		public String getField() { return null;}
	}
}
