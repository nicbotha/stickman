package au.web.odata.entityProcessor.mapper;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpMethod;

import au.model.entity.BaseEntity;

public interface Mapper<K extends BaseEntity> {
	
	public K toJPAEntity(Entity odata) throws IllegalArgumentException;
	
	public Entity toOlingoEntity(K jpa) throws IllegalArgumentException;
	
	public void copyInto(Entity odata, K jpa, HttpMethod httpMethod) throws IllegalArgumentException;
	
	public default Property createPrimitive(final String name, final Object value) {
		return new Property(null, name, ValueType.PRIMITIVE, value);
	}
	
	public default URI createId(String entitySetName, Object id) {
		try {
			return new URI(entitySetName + "(" + String.valueOf(id) + ")");
		} catch (URISyntaxException e) {
			throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
		}
	}
}
