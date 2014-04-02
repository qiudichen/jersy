package com.e2.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "com.iex.tv.smartsync.ws.agent.vacationimport", name = "agentVacation", propOrder = { "messages",
        "importStatus", "agent", "vacation" })
@XmlRootElement(name = "agentVacation")
public class AgentVacation
{
    @XmlElement(required = true)
    protected Agent agent;
    
    @XmlElement(required = true)
    protected List<Vacation> vacation;

    protected String importStatus;

    protected Set<String> messages;

    public AgentVacation()
    {

    }

    /**
     * Gets the value of the agent property.
     * 
     * @return possible object is {@link Agent }
     */
    public Agent getAgent()
    {
        return agent;
    }

    /**
     * Sets the value of the agent property.
     * 
     * @param value allowed object is {@link Agent }
     */
    public void setAgent(Agent value)
    {
        this.agent = value;
    }

    /**
     * Gets the value of the vacation property.
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the vacation property.
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getVacation().add(newItem);
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Vacation }
     */
    public List<Vacation> getVacation()
    {
        if (vacation == null)
        {
            vacation = new ArrayList<Vacation>();
        }
        return this.vacation;
    }

    /**
     * @param messagesParm The messages to set.
     */
    public void setMessages(Set<String> messagesParm)
    {
        messages = messagesParm;
    }

    /**
     * Add error message to agent
     * 
     * @param messageParm
     */
    public void addMessage(String messageParm)
    {
    	if(messages == null) {
    		messages = new HashSet<String>();
    	}
        messages.add(messageParm);
    }

    /**
     * @return Returns the messages.
     */
    public Set<String> getMessages()
    {
        return messages;
    }

    /**
     * @param vacationParm The vacation to set.
     */
    public void setVacation(List<Vacation> vacationParm)
    {
        vacation = vacationParm;
    }

    /**
     * @return Returns the importStatus.
     */
    public String getImportStatus()
    {
        return importStatus;
    }

    /**
     * @param importStatusParm The importStatus to set.
     */
    public void setImportStatus(String importStatusParm)
    {
        importStatus = importStatusParm;
    }

}
