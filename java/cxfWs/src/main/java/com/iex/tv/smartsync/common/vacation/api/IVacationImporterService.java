package com.iex.tv.smartsync.common.vacation.api;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;

import com.iex.tv.smartsync.domain.vacation.AgentVacationSummary;

@WebService(targetNamespace = "com.iex.tv.smartsync.ws.agent.vacationimport", name = "vacationImport")
public interface IVacationImporterService
{
    @WebMethod(operationName = "importVacation")
    public String importAgentVacation(
            @WebParam(name = "vacationSummaries", mode = Mode.IN) AgentVacationSummary agentVacationSummary,
            @WebParam(name = "importProps", mode = Mode.IN) String vacPropParam)
            throws IllegalArgumentException;

}

