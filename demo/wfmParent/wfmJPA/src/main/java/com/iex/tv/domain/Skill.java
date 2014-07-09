package com.iex.tv.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="SKILL")

@NamedQueries({
    @NamedQuery(name = Skill.NamedQuery.QUERY_FIND_BY_NAME, query="SELECT a FROM Skill a WHERE a.name like :name "),
    @NamedQuery(name = Skill.NamedQuery.QUERY_FIND_BY_RANK, query="SELECT a FROM Skill a WHERE (SELECT COUNT(b) FROM Skill b WHERE a.rank > b.rank) = 1"),
    @NamedQuery(name = Skill.NamedQuery.QUERY_GET_MAX_RANK, query="SELECT MAX(b.rank) FROM Skill b")
}) 

public class Skill extends CreateDateEntity {
	@Id 
	@Column(name="SKILL_ID")
	@SequenceGenerator(name="seqSkillId", sequenceName="SEQ_SKILL_ID", allocationSize = 5, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seqSkillId")
	private long id;

	@Column(name="SKILLNAME", nullable = false, length = 40, unique=true)
	private String name;

	@Column(name="RANK", nullable = false, unique=true)
	private Integer rank;
	
	public Skill() {
		super();
	}

	public Skill(String name, Integer rank) {
		super();
		this.name = name;
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
	
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public interface NamedQuery {
		public static final String QUERY_FIND_BY_NAME = "skill.findByName";
		public static final String QUERY_FIND_BY_RANK = "skill.findByRank";
		public static final String QUERY_GET_MAX_RANK = "skill.getMaxRank";
	}	
}
