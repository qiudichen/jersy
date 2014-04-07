package com.iex.tv.domain.training;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-07T01:44:13.140-0500")
@StaticMetamodel(Address.class)
public class Address_ {
	public static volatile SingularAttribute<Address, Long> id;
	public static volatile SingularAttribute<Address, String> street;
	public static volatile SingularAttribute<Address, String> city;
	public static volatile SingularAttribute<Address, String> state;
	public static volatile SingularAttribute<Address, String> country;
	public static volatile SingularAttribute<Address, String> zip;
	public static volatile SingularAttribute<Address, Agent> agent;
}
