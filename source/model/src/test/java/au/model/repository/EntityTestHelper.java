package au.model.repository;

import au.model.entity.SomeEntity;

public class EntityTestHelper {

	public static SomeEntity newSomeEntity() {
		SomeEntity linx = new SomeEntity();
		linx.setName("name");
		return linx;
	}
}
