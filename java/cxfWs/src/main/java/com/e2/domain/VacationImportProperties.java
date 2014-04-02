package com.e2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "vacationImportProperties")
//@XmlType(namespace = "com.iex.tv.smartsync.ws.agent.vacationImportProperties", name = "vacationImportProperties")

public class VacationImportProperties extends ImporterProperties
{
    /**
     * Base property key for agent importer
     */
    private static final String BASE_PROPERTY = "vacation.importer.";

    /**
     * (-f) Path of import file
     */
    protected static final String IMPORT_FILE_PATH = BASE_PROPERTY + "file.path";

    /**
     * Command line arguments
     */
    protected static final String fileToImport = "f";

    /**
     * CTOR for new object
     */
    public VacationImportProperties()
    {
        super();

        Option filePath = new Option(fileToImport, true, "File to Import");
        filePath.setRequired(false);

        Option agentMatch = new Option(agentMatchCriteria, true, "Agent Match Criteria");
        agentMatch.setRequired(false);

        addOption(filePath);
        addOption(agentMatch);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.iex.tv.smartsync.common.properties.ImporterProperties#setCommandLineProperties(org.apache.commons.cli.CommandLine
     * )
     */
    @Override
    public void setCommandLineProperties(CommandLine cmdLineParm)
    {
        super.setCommandLineProperties(cmdLineParm);

        if (cmdLineParm.hasOption(fileToImport))
        {
            setImportFilePath(cmdLineParm.getOptionValue(fileToImport));
        }

        if (cmdLineParm.hasOption(agentMatchCriteria))
        {
            setMatchCriteria(cmdLineParm.getOptionValue(agentMatchCriteria));
        }
    }

    /*
     * (non-Javadoc)
     * @see com.iex.tv.smartsync.common.properties.ImporterProperties#setMatchCriteria(java.lang.String)
     */
    @Override
    public void setMatchCriteria(String matchCriteriaParm)
    {
        if (matchCriteriaParm != null )
        {
            String lower = matchCriteriaParm.toLowerCase();

            if (lower.equals(MATCH_AGENT_ID))
            {
                setMatchCriteriaAgentId(Boolean.TRUE);
            }

            if (lower.equals(MATCH_ACD_LOGON))
            {
                setMatchCriteriaAcdLogon(Boolean.TRUE);
            }

            if (lower.equals(MATCH_SSN))
            {
                setMatchCriteriaSsn(Boolean.TRUE);
            }

            if (lower.equals(MATCH_EXTERNAL_ID))
            {
                setMatchCriteriaExternalId(Boolean.TRUE);
            }
        }
    }

    /**
     * @param importfilePathParm the import file path to set
     */
    public void setImportFilePath(String importfilePathParm)
    {
        properties.setProperty(IMPORT_FILE_PATH, importfilePathParm);
    }

    /**
     * @returns the import file path
     */
    public String getImportFilePath()
    {
        String path = null;

        if (properties.containsKey(IMPORT_FILE_PATH))
        {
            path = properties.getProperty(IMPORT_FILE_PATH);
        }

        return path;
    }

    /*
     * (non-Javadoc)
     * @see com.iex.tv.smartsync.common.properties.ImporterProperties#getRunCommand()
     */
    @Override
    public String getRunCommand()
    {
        return "importVacSummary";
    }
}
