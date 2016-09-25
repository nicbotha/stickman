package au.web.odata.entityProcessor.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.model.entity.SomeEntity;
import au.model.repository.SomeCRUDRepository;
import au.web.odata.ODataConst;
import au.web.odata.entityProcessor.mapper.SomeEntityMapper;

@Component
public class SomeEntityProcessor implements EntityProcessor<SomeCRUDRepository, SomeEntity> {

	@Autowired
	protected SomeCRUDRepository repository;
	
	@Autowired
	protected SomeEntityMapper mapper;
	
	public SomeCRUDRepository getRepository() {
		return repository;
	}
	
	public SomeEntityMapper getMapper() {
		return mapper;
	}

	public String getEntityTypeName() {
		return ODataConst.SOMEENTITY_ET_NAME;
	}

	public String getEntitySetName() {
		return ODataConst.SOMEENTITY_ES_NAME;
	}
	
}
