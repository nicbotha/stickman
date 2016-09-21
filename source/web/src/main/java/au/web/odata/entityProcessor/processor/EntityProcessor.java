package au.web.odata.entityProcessor.processor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.springframework.data.repository.CrudRepository;

import au.model.entity.BaseEntity;
import au.web.odata.ODataConst;

@SuppressWarnings("rawtypes")
public interface EntityProcessor <K extends CrudRepository, L extends BaseEntity>{

	public String getEntitySetName();
	public String getEntityTypeName();
	public K getRepository();
	public Entity toOlingoEntity(L fromEntity);

	@SuppressWarnings("unchecked")
	public default EntityCollection findAll() {
		EntityCollection entities = new EntityCollection();
		Iterable<L> data = getRepository().findAll();

		IteratorUtils.forEach(data.iterator(), e -> {
			entities.getEntities().add(toOlingoEntity(e));
		});

		return entities;
	}

	@SuppressWarnings("unchecked")
	public default Entity read(final EdmEntitySet edmEntitySet, final List<UriParameter> keys) throws EntityProcessorException {
		final EdmEntityType entityType = edmEntitySet.getEntityType();
		final UriParameter key = keys.get(0);

		if (!(isEntityType(entityType) && isPrimaryKey(key))) {
			throw new EntityProcessorException("Could not find record.", new EdmPrimitiveTypeException("Either wrong Entity or incorrect Key."));
		}

		return toOlingoEntity((L) getRepository().findOne(key.getText()));
	}
		
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
	
	public default boolean isPrimaryKey(UriParameter uriParam) {
		return (uriParam.getName().equals(ODataConst.JPA_ENTITY_PKEY));
	}
	
	public default boolean isEntityType(EdmEntityType entityType) {
		return (entityType.getName().equals(getEntityTypeName()));
	}
}
