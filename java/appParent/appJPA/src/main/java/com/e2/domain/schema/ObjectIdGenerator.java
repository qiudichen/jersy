package com.e2.domain.schema;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
//import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;

import com.e2.domain.IdEntity;

public class ObjectIdGenerator implements IdentifierGenerator {
	
	private IdentifierGenerator baseGenerator = new UUIDHexGenerator();
	
	@Override
	public Serializable generate(SessionImplementor sessionImplParm, Object objectParm)
			throws HibernateException {
		
        Serializable oid = null;
		if (objectParm instanceof IdEntity) {
			oid = ((IdEntity) objectParm).getOid();
		}

		if (oid == null) {
			oid = baseGenerator.generate(sessionImplParm, objectParm);
		}
        return oid;
	}
}
