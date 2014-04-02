package com.e2.dao;

import javax.persistence.TemporalType;

public class QueryParameter {
    private final Object value; // this class should be a generic type
	private final TemporalType type; // optional
	private final String parameterName;
	private final int postion;

	public QueryParameter(final Object value, final String parameterName) {
		this(value, parameterName, null, -1);
	}	
	
	public QueryParameter(final Object value, final int positon) {
		this(value, null, null, positon);
	}	
	
	public QueryParameter(final Object value, final String parameterName, final TemporalType type) {
		this(value, parameterName, type, -1);
	}	
	
	public QueryParameter(final Object value, final int positon, final TemporalType type) {
		this(value, null, type, positon);
	}	
	
	private QueryParameter(final Object value, final String parameterName, final TemporalType type, final int positon) {
		this.value = value;
		this.type = type;
		this.parameterName = parameterName;
		this.postion = positon;
	}

	public Object getValue() {
		return value;
	}

	public TemporalType getType() {
		return type;
	}

	public String getParameterName() {
		return parameterName;
	}

	public int getPostion() {
		return postion;
	}	
	
	public boolean isTemporalType() {
		return type != null;
	}
	
	public boolean hasParameterName() {
		return this.parameterName != null;
	}
}
