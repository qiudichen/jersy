package com.iex.tv.services;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.iex.tv.domain.Skill;

public class SkillServiceTest extends BaseServiceTest {
	
	@Resource (name="skillServiceImpl")
	private SkillService service;

	@Test
	public void fullCycleTest() {
		String name = "skill one";
		
		long skillid;
		try {
			skillid = service.addSkill(new Skill(name, 11));
			Assert.assertTrue(skillid > 0);

			Skill skill = service.getSkill(skillid);
			Assert.assertTrue(name.equals(skill.getName()));

			skillid = service.addSkill(new Skill("skill xxxx", 21));
			Assert.assertTrue(skillid > 0);

			List<Skill> skills = service.findSkillByName("skill");
			Assert.assertTrue(skills.size() == 2);

			skill.setName("skill two");
			Skill updatedSkill = service.updateSkill(skill);
			Assert.assertTrue("skill two".equals(updatedSkill.getName()));

			updatedSkill = service.updateSkill(skill.getId(), "skill three");
			Assert.assertTrue("skill three".equals(updatedSkill.getName()));
			service.update(skill.getId(), "skill four");

			updatedSkill = service.getSkill(skill.getId());
			Assert.assertTrue("skill four".equals(updatedSkill.getName()));

			for(int i = 0; i < 10; i++) {
				int rant = i * 3 + 1;
				service.addSkill(new Skill(name + i, rant));
			}
			
			Integer rank = service.getMaxRank();
			Skill skill1 = service.getSkillByRank();
			
			
			updatedSkill = service.updateSkill(skill);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//service.deleteSkill(updatedSkill.getId());
		//service.deleteSkill(skillid);
	}
}
