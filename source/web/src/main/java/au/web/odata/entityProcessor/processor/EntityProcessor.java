package au.web.odata.entityProcessor.processor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import au.model.entity.BaseEntity;
import au.web.odata.ODataConst;
import au.web.odata.entityProcessor.EntityProcessorException;

@SuppressWarnings("rawtypes")
public interface EntityProcessor<K extends CrudRepository, L extends BaseEntity> {

	static final Logger log = LoggerFactory.getLogger(EntityProcessor.class);

	public String getEntitySetName();

	public String getEntityTypeName();

	public K getRepository();

	public Entity toOlingoEntity(L fromEntity);

	public L toJPAEntity(Entity entity);

	public void copyInto(Entity fromEntity, L toEntity, HttpMethod httpMethod);

	@SuppressWarnings("unchecked")
	public default Entity create(EdmEntitySet edmEntitySet, Entity requestEntity) throws ODataApplicationException {
		if (requestEntity != null) {
			L someEntity = toJPAEntity(requestEntity);
			try {
				someEntity = (L) getRepository().save(someEntity);
				return toOlingoEntity(someEntity);
			} catch (Exception e) {
				exceptionHandler(e);
			}

		}
		return null;
	}

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
	public default Entity findOne(final EdmEntitySet edmEntitySet, final List<UriParameter> keys) throws EntityProcessorException {
		final EdmEntityType entityType = edmEntitySet.getEntityType();
		final UriParameter key = keys.get(0);

		if (!(isEntityType(entityType) && isPrimaryKey(key))) {
			throw new EntityProcessorException("Could not find entity.", new EdmPrimitiveTypeException("Either wrong Entity or incorrect Key."));
		}

		return toOlingoEntity((L) getRepository().findOne(key.getText()));
	}

	@SuppressWarnings("unchecked")
	public default void update(EdmEntitySet edmEntitySet, List<UriParameter> keyParams, Entity updateEntity, HttpMethod httpMethod) throws ODataApplicationException {
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		final UriParameter key = keyParams.get(0);

		if (!(isEntityType(edmEntityType) && isPrimaryKey(key))) {
			throw new ODataApplicationException("Could not find entity.", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
		}

		L jpaEntity = (L) getRepository().findOne(key.getText());

		if (jpaEntity == null) {
			throw new ODataApplicationException("Could not find entity.", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
		}

		copyInto(updateEntity, jpaEntity, httpMethod);

		try {
			getRepository().save(jpaEntity);
		} catch (Exception e) {
			exceptionHandler(e);

		}
	}

	public default void exceptionHandler(Exception e) throws ODataApplicationException {
		if (e.getCause() != null && e.getCause() instanceof RollbackException) {
			final RollbackException re = (RollbackException) e.getCause();

			if (re.getCause() != null && re.getCause() instanceof ConstraintViolationException) {
				constraintViolationExceptionHandler((ConstraintViolationException) re.getCause());
			}
		} else if (e instanceof ConstraintViolationException) {
			constraintViolationExceptionHandler((ConstraintViolationException) e);
		} else {
			throw new ODataApplicationException(e.getMessage(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, e);
		}
	}

	public default void constraintViolationExceptionHandler(final ConstraintViolationException ve) throws ODataApplicationException {
		Set<ConstraintViolation<?>> violations = ve.getConstraintViolations();
		StringBuffer sb = new StringBuffer();

		IteratorUtils.forEach(violations.iterator(), v -> {
			sb.append(v.getMessage());
		});

		throw new ODataApplicationException(sb.toString(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, ve);
	}

	@SuppressWarnings("unchecked")
	public default void delete(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		final UriParameter key = keyParams.get(0);

		if (!(isEntityType(edmEntityType) && isPrimaryKey(key))) {
			throw new ODataApplicationException("Could not delete entitiy", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
		}

		getRepository().delete(key.getText());
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
