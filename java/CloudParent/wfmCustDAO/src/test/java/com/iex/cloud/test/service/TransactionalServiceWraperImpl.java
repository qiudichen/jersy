package com.iex.cloud.test.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("transactionalServiceWraperImpl")
public class TransactionalServiceWraperImpl implements
		TransactionalServiceWraper {

	@Override
	public Object doExecute(CallbackService action) throws Exception {
		return action.execute();
	}
}
