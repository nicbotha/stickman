package au.model.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.model.entity.SomeEntity;
import au.model.repository.SomeCRUDRepository;

public class TestSomeCRUDRepository extends BaseRepositoryTestCase<SomeCRUDRepository, SomeEntity> {
	private static final String NAME_CONSTRAINT_VIOLATION = "abcdefghijklmnopqrstuvwxyz123456789";
	protected final Logger log = LoggerFactory.getLogger(TestSomeCRUDRepository.class);

	@Override
	public SomeEntity newEntity() {
		SomeEntity linx = new SomeEntity();
		linx.setName("name");
		return linx;
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
	public void testConstraints_NameSize() throws Exception {
		SomeEntity linx = newEntity();
		linx.setName(NAME_CONSTRAINT_VIOLATION);
		boolean exception = false;
		try {
			getRepository().save(linx);
		} catch (ConstraintViolationException e) {
			exception = true;
			Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
			
			for(@SuppressWarnings("rawtypes") ConstraintViolation v : violations) {
				assertThat("Unexpected error message.",v.getMessage(), equalTo(getI18n("model.someentity.name.size")));
			}
			
		}
		assertTrue("Unexpected result status: Bean validation failed.",exception);
	}
	
	@Test
	public void testConstraints_NameNull() throws Exception {
		SomeEntity linx = newEntity();
		linx.setName(null);
		boolean exception = false;
		try {
			getRepository().save(linx);
		} catch (ConstraintViolationException e) {
			exception = true;
			Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
			
			for(@SuppressWarnings("rawtypes") ConstraintViolation v : violations) {
				assertThat("Unexpected error message.",v.getMessage(), equalTo(getI18n("model.someentity.name.notnull")));
			}
			
		}
		
		assertTrue("Unexpected result status: Bean validation failed.",exception);
	}

}
