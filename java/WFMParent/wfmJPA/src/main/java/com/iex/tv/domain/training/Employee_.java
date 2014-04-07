package com.iex.tv.domain.training;

import com.iex.tv.domain.training.Employee.Gender;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-07T00:37:35.129-0500")
@StaticMetamodel(Employee.class)
public class Employee_ extends CreateDateEntity_ {
	public static volatile SingularAttribute<Employee, Long> empNum;
	public static volatile SingularAttribute<Employee, Date> birthDate;
	public static volatile SingularAttribute<Employee, String> firstName;
	public static volatile SingularAttribute<Employee, String> lastName;
	public static volatile SingularAttribute<Employee, Gender> gender;
	public static volatile SingularAttribute<Employee, Date> hireDate;
}
