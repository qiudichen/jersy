package com.iex.tv.domain.customer;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-03T11:22:57.562-0500")
@StaticMetamodel(Address.class)
public class Address_ extends CreateDateEntity_ {
	public static volatile SingularAttribute<Address, Long> id;
	public static volatile SingularAttribute<Address, String> addrs1;
	public static volatile SingularAttribute<Address, String> addrs2;
	public static volatile SingularAttribute<Address, String> city;
	public static volatile SingularAttribute<Address, String> state;
	public static volatile SingularAttribute<Address, String> zip;
	public static volatile SingularAttribute<Address, Agent> agent;
}
