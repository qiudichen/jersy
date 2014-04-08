package com.iex.tv.demo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.iex.tv.domain.training.Skill;

@Repository("skillDaoImpl")
public class SkillDaoImpl implements SkillDao {
	@PersistenceContext(name="emDemo") 
	protected EntityManager em;
	
	@Override
	public List<Skill> getSkills() {
		TypedQuery<Skill> q = em.createQuery("SELECT e FROM Skill e", Skill.class);
    	List<Skill> result = q.getResultList();
    	return result;
	}

	@Override
	public Skill addSkill(String skillName) {
		Skill skill = new Skill(skillName);
		em.persist(skill);
		return skill;
	}

	@Override
	public Skill findByPk(long id) {
		return em.find(Skill.class, id);
	}

	@Override
	public void deleteSkill(long id) {
		Skill skill = em.getReference(Skill.class, id);
		em.remove(skill);
	}

	@Override
	public void deleteSkill(Skill skill) {
		if(skill != null) {
			em.remove(em.contains(skill) ? skill : em.merge(skill));
		}
	}

	@Override
	public Skill updateSkill(Skill skill, String skillName) {
		Skill managedSkill = em.merge(skill);
		managedSkill.setName(skillName);
		return managedSkill;
	}

	@Override
	public Skill updateSkill(long skillId, String skillName) {
		Skill skill = em.getReference(Skill.class, skillId);
		skill.setName(skillName);
		return skill;
	}

}
