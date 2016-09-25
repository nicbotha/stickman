package au.web.odata.entityProcessor.mapper;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.springframework.stereotype.Component;

import au.model.entity.SomeEntity;
import au.web.odata.ODataConst;

@Component
public class SomeEntityMapper implements Mapper<SomeEntity> {
	
	public static final String NAME_P = "Name";

	@Override
	public SomeEntity toJPAEntity(Entity from) {

		if (from != null) {
			SomeEntity to = new SomeEntity();
			copyInto(from, to, HttpMethod.PATCH);
			return to;
		}
		return null;
	}

	public Entity toOlingoEntity(SomeEntity from) {

		if (from != null) {
			Entity to = new Entity();
			to.addProperty(createPrimitive(ODataConst.JPA_ENTITY_PKEY, new Integer(from.getId())));
			to.addProperty(createPrimitive(SomeEntityMapper.NAME_P, from.getName()));
			to.setId(createId(ODataConst.SOMEENTITY_ES_NAME, from.getId()));
			return to;
		}

		return null;
	}

	public void copyInto(Entity from, SomeEntity to, HttpMethod httpMethod) {
		Property nameProperty = from.getProperty(SomeEntityMapper.NAME_P);

		if(from == null || to == null || httpMethod ==  null) {
			return;
		}
		
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
