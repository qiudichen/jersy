package com.iex.tv.services;

import java.util.List;

import com.iex.tv.domain.Skill;

public interface SkillService {
	public Skill getSkill(long skillId);
	
	public List<Skill> findSkillByName(String name);
	
	public long addSkill(Skill skill);
	
	public void deleteSkill(long skillId);
	
	public Skill updateSkill(long skillId, String name);
	
	public Skill updateSkill(Skill skill);
	
	public void update(long skillId, String name);
	
	public Skill getSkillByRank();
	
	public Integer getMaxRank();
}
