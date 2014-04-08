package com.iex.tv.demo.services;

import java.util.List;

import com.iex.tv.domain.training.Skill;

public interface SkillService {
	public List<Skill> getSkills();
	
	public Skill getSkill(long id);
	
	public Skill addSkill(String skillName) throws DBRollback;
	
	public void deleteSkill(long id) throws DBRollback;
	
	public void deleteSkill(Skill skill) throws DBRollback;
	
	public Skill updateSkill(long id, String skillName) throws DBRollback;
	
	public Skill updateSkill(Skill skill, String skillName) throws DBRollback;
}
