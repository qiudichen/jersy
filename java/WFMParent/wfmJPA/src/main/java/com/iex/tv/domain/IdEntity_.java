package com.iex.tv.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.Instant;

@Generated(value="Dali", date="2014-04-02T01:28:28.196-0500")
@StaticMetamodel(IdEntity.class)
public class IdEntity_ {
	public static volatile SingularAttribute<IdEntity, String> oid;
	public static volatile SingularAttribute<IdEntity, Instant> version;
}
