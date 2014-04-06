package com.iex.tv.domain.system;

import com.iex.tv.domain.IdEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-02T14:28:19.914-0500")
@StaticMetamodel(SystemProperty.class)
public class SystemProperty_ extends IdEntity_ {
	public static volatile SingularAttribute<SystemProperty, SubSystemType> subsystem;
	public static volatile SingularAttribute<SystemProperty, String> name;
	public static volatile SingularAttribute<SystemProperty, String> value;
}
