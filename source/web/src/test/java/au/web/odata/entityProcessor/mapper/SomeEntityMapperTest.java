package au.web.odata.entityProcessor.mapper;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;

import java.util.Random;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.model.entity.SomeEntity;
import au.model.repository.EntityTestHelper;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/testContext.xml" })
@Profile("test")
public class SomeEntityMapperTest {

	@Autowired
	SomeEntityMapper underTestMapper;
	
	@Test
	public void testMappings() throws Exception{
		SomeEntity jpa = EntityTestHelper.newSomeEntity();
		jpa.setId(String.valueOf(new Random().nextInt()));
		
		/** JPA -> Olingo*/
		Entity odata = underTestMapper.toOlingoEntity(jpa);
		
		assertThat("name", equalTo(odata.getProperty(SomeEntityMapper.NAME_P).getValue()));
		
		/** Olingo -> JPA*/
		SomeEntity jpa_gen = underTestMapper.toJPAEntity(odata);
		
		assertThat("name", equalTo(jpa_gen.getName()));
		
		/** Olingo -> JPA : HTTP.PATCH && HTTP.GET */
		SomeEntity nJPA = new SomeEntity();
		Entity nE = new Entity();
		
		underTestMapper.copyInto(nE, nJPA, HttpMethod.PATCH);
		assertNull("PATCH test failure", nJPA.getName());
		
		underTestMapper.copyInto(nE, nJPA, HttpMethod.GET);
		assertNull("GET test failure", nJPA.getName());
	}
}
