package com.iex.tv.demo.dao;

import java.util.List;

import com.iex.tv.domain.training.Skill;

public interface SkillDao {
	public List<Skill> getSkills();
	
	public Skill addSkill(String skillName);
	
	public Skill findByPk(long id);
	
	public void deleteSkill(long id);
	
	public void deleteSkill(Skill skill);
	
	public Skill updateSkill(Skill skill, String skillName);
	
	public Skill updateSkill(long skillId, String skillName);
}
