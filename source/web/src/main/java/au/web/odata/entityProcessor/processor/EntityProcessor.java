package au.web.odata.entityProcessor.processor;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
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
import au.web.odata.entityProcessor.mapper.Mapper;

@SuppressWarnings("rawtypes")
public interface EntityProcessor<K extends CrudRepository, L extends BaseEntity> {

	static final Logger log = LoggerFactory.getLogger(EntityProcessor.class);

	public String getEntitySetName();

	public String getEntityTypeName();

	public K getRepository();

	public Mapper<L> getMapper();

	@SuppressWarnings("unchecked")
	public default Entity create(EdmEntitySet edmEntitySet, Entity requestEntity) throws ODataApplicationException {
		if (requestEntity != null) {
			L someEntity = null;
			try {
				someEntity = getMapper().toJPAEntity(requestEntity);
			} catch (IllegalArgumentException e) {
				log.error("Could not map OData to entity.",e);
				throw new ODataApplicationException("Could not map OData to entity.",HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, e);
			}
			
			try {
				someEntity = (L) getRepository().save(someEntity);
				return getMapper().toOlingoEntity(someEntity);
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
			entities.getEntities().add(getMapper().toOlingoEntity(e));
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

		return getMapper().toOlingoEntity((L) getRepository().findOne(key.getText()));
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

		getMapper().copyInto(updateEntity, jpaEntity, httpMethod);

		try {
			getRepository().save(jpaEntity);
		} catch (Exception e) {
			exceptionHandler(e);

		}
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

	public default void exceptionHandler(Exception e) throws ODataApplicationException {
		log.error("Could not persist " + getEntityTypeName(), e);
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

	public default boolean isPrimaryKey(UriParameter uriParam) {
		return (uriParam.getName().equals(ODataConst.JPA_ENTITY_PKEY));
	}

	public default boolean isEntityType(EdmEntityType entityType) {
		return (entityType.getName().equals(getEntityTypeName()));
	}
}
