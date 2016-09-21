package au.web.odata.entityProcessor.processor;

import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.model.entity.SomeEntity;
import au.model.repository.SomeCRUDRepository;
import au.web.odata.ODataConst;

@Component
public class SomeEntityProcessor implements EntityProcessor<SomeCRUDRepository, SomeEntity> {

	@Autowired
	protected SomeCRUDRepository repository;

	public Entity toOlingoEntity(SomeEntity fromEntity) {
		Entity el = new Entity();
		if (fromEntity != null) {
			el.addProperty(createPrimitive(ODataConst.JPA_ENTITY_PKEY, new Integer(fromEntity.getId())))
			.addProperty(createPrimitive("Name", fromEntity.getName()))
			.setId(createId(getEntitySetName(), fromEntity.getId()));
			return el;
		}

		return null;
	}
	
	public SomeCRUDRepository getRepository() {
		return repository;
	}

	public String getEntityTypeName() {
		return ODataConst.SOMEENTITY_ET_NAME;
	}

	public String getEntitySetName() {
		return ODataConst.SOMEENTITY_ES_NAME;
	}

	
}
