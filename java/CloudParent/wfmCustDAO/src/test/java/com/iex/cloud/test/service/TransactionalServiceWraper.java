package com.iex.cloud.test.service;

public interface TransactionalServiceWraper {
	public Object doExecute(CallbackService action) throws Exception;
}
