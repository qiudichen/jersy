package com.iex.cloud.dao;

import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Service("transactionServiceImpl")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Override
	public Object run(ServiceCallBack callBack)  throws Exception {
		return callBack.execute();
	}
}
