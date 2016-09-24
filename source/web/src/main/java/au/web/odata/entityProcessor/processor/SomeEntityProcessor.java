package au.web.odata.entityProcessor.processor;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.model.entity.SomeEntity;
import au.model.repository.SomeCRUDRepository;
import au.web.odata.ODataConst;

@Component
public class SomeEntityProcessor implements EntityProcessor<SomeCRUDRepository, SomeEntity> {

	@Autowired
	protected SomeCRUDRepository repository;

	public SomeCRUDRepository getRepository() {
		return repository;
	}

	public String getEntityTypeName() {
		return ODataConst.SOMEENTITY_ET_NAME;
	}

	public String getEntitySetName() {
		return ODataConst.SOMEENTITY_ES_NAME;
	}

	@Override
	public SomeEntity toJPAEntity(Entity from) {

		if (from != null) {
			SomeEntity to = new SomeEntity();
			to.setName((String) from.getProperty("Name").getValue());
			return to;
		}
		return null;
	}

	public Entity toOlingoEntity(SomeEntity from) {

		if (from != null) {
			Entity to = new Entity();
			to.addProperty(createPrimitive(ODataConst.JPA_ENTITY_PKEY, new Integer(from.getId()))).addProperty(createPrimitive("Name", from.getName())).setId(createId(getEntitySetName(), from.getId()));
			return to;
		}

		return null;
	}

	public void copyInto(Entity from, SomeEntity to, HttpMethod httpMethod) {
		Property nameProperty = from.getProperty("Name");

		if (nameProperty == null) {
			if (httpMethod.equals(HttpMethod.PATCH)) {
				// Don't do anything as per OData V4 Spec
			} else if (httpMethod.equals(HttpMethod.PUT)) {
				to.setName(null);
			}
		} else {
			to.setName((String) nameProperty.getValue());
		}
	}

}
