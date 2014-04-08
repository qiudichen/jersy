package com.iex.tv.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.demo.dao.SkillDao;
import com.iex.tv.domain.training.Skill;

@Service("skillServiceImpl")
public class SkillServiceImpl implements SkillService {
	@Autowired
	@Qualifier("skillDaoImpl")	
	private SkillDao skillDao;

	@Override
	@Transactional(value="demoTransactionManager", readOnly=true)
	public List<Skill> getSkills() {
		return skillDao.getSkills();
	}

	@Override
	@Transactional(value="demoTransactionManager", readOnly=true)
	public Skill getSkill(long id) {
		return skillDao.findByPk(id);
	}

	@Override
	@Transactional(value="demoTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=DBRollback.class)
	public Skill addSkill(String skillName) throws DBRollback {
		try {
			Skill skill = skillDao.addSkill(skillName);
			if("testThrow".equals(skillName)) {
				throw new DBRollback("test");
			}
			return skill;
		} catch(Exception e) {
			if(e instanceof DBRollback) {
				throw (DBRollback)e;
			}
			throw new DBRollback(e);
		}
	}

	@Override
	@Transactional(value="demoTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=DBRollback.class)
	public void deleteSkill(long id) throws DBRollback {
		try {
			this.skillDao.deleteSkill(id);
		} catch(Exception e) {
			if(e instanceof DBRollback) {
				throw (DBRollback)e;
			}			
			throw new DBRollback(e);
		}
	}

	@Override
	@Transactional(value="demoTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=DBRollback.class)
	public void deleteSkill(Skill skill) throws DBRollback {
		try {
			this.skillDao.deleteSkill(skill);
		} catch(Exception e) {
			if(e instanceof DBRollback) {
				throw (DBRollback)e;
			}			
			throw new DBRollback(e);
		}
	}

	@Override
	@Transactional(value="demoTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=DBRollback.class)
	public Skill updateSkill(long id, String skillName) throws DBRollback {
		
		try {		
			Skill skill = skillDao.updateSkill(id, skillName);
//			Skill skill = skillDao.findByPk(id);
//			if(skill == null) {
//				return this.skillDao.addSkill(skillName);
//			}
//			
//			skill.setName(skillName);
			return skill;
		} catch(Exception e) {
			if(e instanceof DBRollback) {
				throw (DBRollback)e;
			}
			throw new DBRollback(e);
		}
	}

	@Override
	@Transactional(value="demoTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=DBRollback.class)
	public Skill updateSkill(Skill skill, String skillName) throws DBRollback {
		try {
			return skillDao.updateSkill(skill, skillName);
		} catch(Exception e) {
			if(e instanceof DBRollback) {
				throw (DBRollback)e;
			}
			throw new DBRollback(e);
		}
	}
	
	
}
