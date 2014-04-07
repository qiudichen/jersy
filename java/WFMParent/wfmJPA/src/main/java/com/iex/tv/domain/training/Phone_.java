package com.iex.tv.domain.training;

import com.iex.tv.domain.BaseEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-07T00:43:06.261-0500")
@StaticMetamodel(Phone.class)
public class Phone_ extends BaseEntity_ {
	public static volatile SingularAttribute<Phone, Long> id;
	public static volatile SingularAttribute<Phone, String> phone;
	public static volatile SingularAttribute<Phone, Long> agentId;
}
