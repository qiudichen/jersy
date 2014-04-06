package com.iex.tv.domain.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.iex.tv.domain.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="R_SKILL")
public class Skill extends BaseEntity {
	@Id 
	@Column(name="C_ID")
	@SequenceGenerator(name="seqSkillId", sequenceName="SEQ_SKILL_ID", allocationSize = 5, initialValue = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seqSkillId")
	private long id;

	@Column(name="C_SKILLNAME", nullable = false, length = 40, unique=true)
	private String name;
	
	public Skill() {
		super();
	}
	
	
}
