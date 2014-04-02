package com.e2.webservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iex.tv.smartsync.common.vacation.api.IVacationImporterService;
import com.iex.tv.smartsync.domain.vacation.AgentVacationSummary;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/client-ws-config.xml"})
public class WSClient {
	@Autowired
	private IVacationImporterService service;

	@Test
	public void importAgentVacation() {
		try {
			AgentVacationSummary parm = new AgentVacationSummary();
		
			String result = service.importAgentVacation(parm, "parm2");
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
