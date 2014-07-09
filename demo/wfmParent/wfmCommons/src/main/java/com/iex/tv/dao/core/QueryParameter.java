package com.iex.tv.dao.core;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Query;
import javax.persistence.TemporalType;

public class QueryParameter {
	private enum DataType {
		OBJECT, DATE, CALENDAR;
		
		static DataType getTemporalDataType(Object value) {
			if(value instanceof Date) {
				return DATE;
			}
			
			if(value instanceof Calendar) {
				return CALENDAR;
			}			
			return null;
		}
	};
	
	//value to bind the parameter in JSQL
	private final Object value; 
	
	//optional, only if data type is date or timestamp
	private final TemporalType type; // optional
	
	//below two variables, only one variable is need
	
	//parameter name in JSQL
	private final String parameterName;
	
	//position in JSQL
	private final int postion;

	private DataType dataType;
	
	public QueryParameter(final Object value, final String parameterName) {
		this(value, parameterName, null);
	}

	public QueryParameter(final Object value, final int positon) {
		this(value, positon, null);
	}

	public QueryParameter(final Object value, final String parameterName, final TemporalType type) {
		this(value, parameterName, -1, type);
	}

	public QueryParameter(final Object value, final int positon, final TemporalType type) {
		this(value, null, positon, type);
	}

	private QueryParameter(final Object value, final String parameterName, final int positon, final TemporalType type) {
		this.value = value;
		this.type = type;	
		this.parameterName = parameterName;
		this.postion = positon;
		
		if(this.parameterName == null && this.postion < 1) {
			throw new RuntimeException("One of parameter name or position must be specified ");
		}
		
		if(this.value == null) {
			throw new RuntimeException("parameter value cannot be null.");
		}
		
		validDataValue();
	}
	
	private void validDataValue() {
		if(this.type == null) {
			this.dataType = DataType.OBJECT;
			return;
		}
		
		this.dataType = DataType.getTemporalDataType(this.value);
		
		if(this.dataType == null) {
			throw new RuntimeException("Invalide temporal type value: " + this.value.getClass().getName());
		}
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

	public void populateQueryNamedParameter(Query query) {
		if(hasParameterName()) {
			populateQueryNamedParameter(query, this.parameterName);
		} else {
			populateQueryNamedParameter(query, this.postion);
		}
	}
	
	private void populateQueryNamedParameter(Query query, String parameterName) {
		switch(this.dataType) {
			case OBJECT:
				query.setParameter(parameterName, this.value);
				break;
			case DATE:
				query.setParameter(parameterName, (Date)this.value);
				break;
			case CALENDAR:
				query.setParameter(parameterName, (Calendar)this.value);
				break;
			default:
				query.setParameter(parameterName, this.value);
		}
	}
	
	private void populateQueryNamedParameter(Query query, int position) {
		switch (this.dataType) {
		case OBJECT:
			query.setParameter(position, this.value);
			break;
		case DATE:
			query.setParameter(position, (Date) this.value);
			break;
		case CALENDAR:
			query.setParameter(position, (Calendar) this.value);
			break;
		default:
			query.setParameter(position, this.value);
		}
	}

	private boolean hasParameterName() {
		return this.parameterName != null;
	}
}