package au.model.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.model.entity.SomeEntity;

public class SomeCRUDRepositoryTest extends BaseRepositoryTestCase<SomeCRUDRepository, SomeEntity> {
	protected final Logger log = LoggerFactory.getLogger(SomeCRUDRepositoryTest.class);

	@Override
	public SomeEntity newEntity() {
		return EntityTestHelper.newSomeEntity();
	}

	@Override
	protected SomeEntity updateTestEntity(SomeEntity linx) {
		linx.setName("AnotherName");
		return linx;
	}

	@Override
	protected void assertIsUpdated(SomeEntity entity) {
		assertThat("AnotherName", equalTo(entity.getName()));
	}

	@Test
	public void testConstraints_Size() throws Exception {
		SomeEntity entity = newEntity();
		
		final String SIZE_CONSTRAINT_VIOLATION = StringUtils.repeat("a", 500);
		
		String[] expected = new String[] {getI18n("model.someentity.name.size")};
		
		entity.setName(SIZE_CONSTRAINT_VIOLATION);		
		
		checkExceptions(entity, expected);
	}
	
	@Test
	public void testConstraints_Null() throws Exception {
		SomeEntity entity = newEntity();
		String[] expected = new String[] {getI18n("model.someentity.name.notnull")};
		
		entity.setName(null);
		
		checkExceptions(entity, expected);
	}

}
