package com.e2.domain;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public abstract class ImporterProperties extends Options {
	private static final String BASE_PROPERTY = "importer.";

    /**
     * (-c) Customer ID of agents being imported
     */
    protected static final String CUSTOMER_ID = BASE_PROPERTY + "customer.id";

    /**
     * (-a t) Use agent ID match agents for ChangeAgent
     */
    protected static final String MATCH_CRITERIA_AGENT_ID = BASE_PROPERTY + "matching.criteria.agent.id";

    /**
     * (-a e) Use externa ID to match agents for ChangeAgent
     */
    protected static final String MATCH_CRITERIA_EXTERNAL_ID = BASE_PROPERTY + "matching.criteria.external.id";

    /**
     * (-a s) Use social security numbers to match agents for ChangeAgent
     */
    protected static final String MATCH_CRITERIA_SSN = BASE_PROPERTY + "matching.criteria.ssn";

    /**
     * (-a a) Use ACD logon to match agents for ChangeAgent
     */
    protected static final String MATCH_CRITERIA_ACD_LOGON = BASE_PROPERTY + "matching.criteria.acd.logon";

    /**
     * Values for -a option
     */
    protected static final String MATCH_AGENT_ID = "t";
    protected static final String MATCH_EXTERNAL_ID = "e";
    protected static final String MATCH_SSN = "s";
    protected static final String MATCH_ACD_LOGON = "a";

    /**
     * Command line arguments
     */
    protected static final String cmdCustomer = "c";
    protected static final String agentMatchCriteria = "a";

    /**
     * Properties to hold given command line values
     */
    protected Properties properties;

    /**
     * CTOR for new object
     */
    public ImporterProperties()
    {
        properties = new Properties();

        Option customer = new Option(cmdCustomer, true, "Customer to Run Import for");
        customer.setRequired(true);

        addOption(customer);
    }

    /**
     * Method to set the command line properties once they have been parsed
     * 
     * @param cmdLineParm
     */
    public void setCommandLineProperties(CommandLine cmdLineParm)
    {
        setCustomerId(cmdLineParm.getOptionValue(cmdCustomer));
    }

    /**
     * Method to add to propeties
     * 
     * @param key
     * @param value
     */
    public void put(Object key, Object value)
    {
        properties.put(key, value);
    }

    /**
     * @param customerIdParm the customer ID to set
     */
    public void setCustomerId(String customerIdParm)
    {
        properties.setProperty(CUSTOMER_ID, customerIdParm);
    }

    /**
     * @returns the customer ID
     */
    public String getCustomerId()
    {
        String customerId = null;

        if (properties.containsKey(CUSTOMER_ID))
        {

            customerId = properties.getProperty(CUSTOMER_ID);
        }

        return customerId;
    }

    /**
     * Method for subclasses to implement to allow specifid match criteria options
     * 
     * @param matchCriteriaParm
     */
    public abstract void setMatchCriteria(String matchCriteriaParm);

    /**
     * @param matchCriteriaAgentIdParm the agent id match criteria is specified
     */
    public void setMatchCriteriaAgentId(Boolean matchCriteriaAgentIdParm)
    {
        properties.setProperty(MATCH_CRITERIA_AGENT_ID, String.valueOf(matchCriteriaAgentIdParm));
    }

    /**
     * @returns TRUE if agent ID match criteria is specified
     */
    public Boolean isMatchCriteriaAgentId()
    {
        Boolean agentIdMatchCriteria = Boolean.FALSE;

        if (properties.containsKey(MATCH_CRITERIA_AGENT_ID))
        {
            agentIdMatchCriteria = Boolean.valueOf(properties.getProperty(MATCH_CRITERIA_AGENT_ID));
        }

        return agentIdMatchCriteria;
    }

    /**
     * @param matchCriteriaExternalIdParm the external ID match criteria is specified
     */
    public void setMatchCriteriaExternalId(Boolean matchCriteriaExternalIdParm)
    {
        properties.setProperty(MATCH_CRITERIA_EXTERNAL_ID, String.valueOf(matchCriteriaExternalIdParm));
    }

    /**
     * @returns TRUE if external ID match criteria is specified
     */
    public Boolean isMatchCriteriaExternalId()
    {
        Boolean externalIdMatchCriteria = Boolean.FALSE;

        if (properties.containsKey(MATCH_CRITERIA_EXTERNAL_ID))
        {
            externalIdMatchCriteria = Boolean.valueOf(properties.getProperty(MATCH_CRITERIA_EXTERNAL_ID));
        }

        return externalIdMatchCriteria;
    }

    /**
     * @param matchCriteriaSsnParm the ssn match criteria is specified
     */
    public void setMatchCriteriaSsn(Boolean matchCriteriaSsnParm)
    {
        properties.setProperty(MATCH_CRITERIA_SSN, String.valueOf(matchCriteriaSsnParm));
    }

    /**
     * @returns TRUE if ssn match criteria is specified
     */
    public Boolean isMatchCriteriaSsn()
    {
        Boolean ssnMatchCriteria = Boolean.FALSE;

        if (properties.containsKey(MATCH_CRITERIA_SSN))
        {
            ssnMatchCriteria = Boolean.valueOf(properties.getProperty(MATCH_CRITERIA_SSN));
        }

        return ssnMatchCriteria;
    }

    /**
     * @param matchCriteriaAcdLogonParm the acd logon match criteria is specified
     */
    public void setMatchCriteriaAcdLogon(Boolean matchCriteriaAcdLogonParm)
    {
        properties.setProperty(MATCH_CRITERIA_ACD_LOGON, String.valueOf(matchCriteriaAcdLogonParm));
    }

    /**
     * @returns TRUE if acd logon match criteria is specified
     */
    public Boolean isMatchCriteriaAcdLogon()
    {
        Boolean acdMatchCriteria = Boolean.FALSE;

        if (properties.containsKey(MATCH_CRITERIA_ACD_LOGON))
        {
            acdMatchCriteria = Boolean.valueOf(properties.getProperty(MATCH_CRITERIA_ACD_LOGON));
        }

        return acdMatchCriteria;
    }

    /**
     * @return Returns the properties.
     */
    public Properties getProperties()
    {
        return properties;
    }

    /**
     * Returns the command to run the importer
     * 
     * @return the command to run the importer
     */
    public abstract String getRunCommand();
}
