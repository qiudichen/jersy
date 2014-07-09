package com.iex.tv.core.config;

import java.lang.reflect.InvocationTargetException;

import javax.sql.DataSource;

public interface IConfigurableDataSourceFactory {
	public DataSource createDataSource(ICustomerContext customerContext) throws IllegalArgumentException, InstantiationException,
    	IllegalAccessException, InvocationTargetException;
}
