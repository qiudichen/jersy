package com.iex.tv.domain.customer;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-03T11:29:07.195-0500")
@StaticMetamodel(Agent.class)
public class Agent_ extends CreateDateEntity_ {
	public static volatile SingularAttribute<Agent, Long> id;
	public static volatile ListAttribute<Agent, Address> addresses;
	public static volatile SingularAttribute<Agent, String> firstName;
	public static volatile SingularAttribute<Agent, String> lastName;
	public static volatile SingularAttribute<Agent, Date> startDate;
	public static volatile ListAttribute<Agent, Skill> skills;
	public static volatile ListAttribute<Agent, Phone> Phones;
}
