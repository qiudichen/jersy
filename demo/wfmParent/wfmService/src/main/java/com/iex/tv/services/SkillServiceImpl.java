package com.iex.tv.services;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.dao.core.EntityCommonDao;
import com.iex.tv.dao.core.QueryParameter;
import com.iex.tv.domain.Skill;

@Service("skillServiceImpl")
@Transactional(value="demoTransactionManager", propagation = Propagation.REQUIRED)
public class SkillServiceImpl implements SkillService {

	@Autowired
	@Qualifier("skillDAO")	
	private EntityCommonDao<Skill, Long> skillDao;
	
	@Override
	public long addSkill(Skill skill) {
		skillDao.persist(skill);
		return skill.getId();
	}

	@Override
	public void deleteSkill(long skillId) {
		skillDao.remove(skillId);
	}

	@Override
	public Skill updateSkill(long skillId, String name) {
		Skill skill = skillDao.findByPk(skillId);
		skill.setName(name);
		return skill;
	}
	
	@Override
	public Skill updateSkill(Skill skill) {
		Skill manager = skillDao.merge(skill);
		return manager;
	}

	@Override
	public void update(long skillId, String name) {
		skillDao.update("Update Skill set name = :name Where id = :id", new QueryParameter(name, "name"), new QueryParameter(skillId, "id"));
	}
	
	@Override
	@Transactional(value="demoTransactionManager", readOnly=true)
	public List<Skill> findSkillByName(String name) {
		if(StringUtils.isEmpty(name)) {
			return Collections.emptyList();
		}
		
		QueryParameter nameQP = new QueryParameter((new StringBuilder(name).append('%')).toString(), "name");
		List<Skill> skills = skillDao.findByNamedQuery(Skill.NamedQuery.QUERY_FIND_BY_NAME, nameQP);
		
		skills = skillDao.find("SELECT a FROM Skill a WHERE a.name like :name", nameQP);
		
		QueryParameter nameQP1 = new QueryParameter((new StringBuilder(name).append('%')).toString(), 1);
		skills = skillDao.findByNativeQuery("SELECT * FROM SKILL a WHERE a.SKILLNAME like ?", nameQP1);
		return skills;
	}

	@Override
	public Skill getSkill(long skillId) {
		return skillDao.findByPk(skillId);
	}

	@Override
	public Skill getSkillByRank() {
		return (Skill)skillDao.findSingleResultByNamedQuery(Skill.NamedQuery.QUERY_FIND_BY_RANK);
	}

	@Override
	public Integer getMaxRank() {
		Number num = (Number)skillDao.findSingleResultByNamedQuery(Skill.NamedQuery.QUERY_GET_MAX_RANK);
		if(num instanceof Integer) {
			return (Integer)num;
		} else {
			return num.intValue();
		}
	}
}
