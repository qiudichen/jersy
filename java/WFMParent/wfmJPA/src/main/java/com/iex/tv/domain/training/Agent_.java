package com.iex.tv.domain.training;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-08T03:25:50.285-0500")
@StaticMetamodel(Agent.class)
public class Agent_ extends CreateDateEntity_ {
	public static volatile SingularAttribute<Agent, Long> id;
	public static volatile SingularAttribute<Agent, Person> person;
	public static volatile SingularAttribute<Agent, Date> startDate;
	public static volatile CollectionAttribute<Agent, Address> addresses;
	public static volatile ListAttribute<Agent, Phone> phones;
	public static volatile ListAttribute<Agent, Skill> skills;
}
