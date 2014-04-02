package com.e2.webservice;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

import com.e2.domain.AgentVacationSummary;
import com.e2.domain.VacationImportProperties;
import com.iex.tv.smartsync.common.vacation.api.IVacationImporterService;

@Service("VacationImporterServiceImpl")
@WebService(endpointInterface = "com.iex.tv.smartsync.common.vacation.api.IVacationImporterService", targetNamespace = "com.iex.tv.smartsync.ws.agent.vacationimport", name = "vacationImport")
public class VacationImporterServiceImpl implements IVacationImporterService
{

	@Override
	public String importAgentVacation(
			com.iex.tv.smartsync.domain.vacation.AgentVacationSummary agentVacationSummary,
			String vacPropParam) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
