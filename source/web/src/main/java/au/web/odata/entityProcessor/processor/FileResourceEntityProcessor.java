package au.web.odata.entityProcessor.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.model.entity.FileResource;
import au.model.repository.FileResourceCRUDRepository;
import au.web.odata.ODataConst;
import au.web.odata.entityProcessor.mapper.FileResourceMapper;
import au.web.odata.entityProcessor.mapper.Mapper;

@Component
public class FileResourceEntityProcessor implements EntityProcessor<FileResourceCRUDRepository, FileResource> {

	@Autowired
	protected FileResourceCRUDRepository repository;
	
	@Autowired
	protected FileResourceMapper mapper;
	
	@Override
	public String getEntitySetName() {
		return ODataConst.FILERESOURCE_ES_NAME;
	}

	public String getEntityTypeName() {
		return ODataConst.FILERESOURCE_ET_NAME;
	}

	public FileResourceCRUDRepository getRepository() {
		return repository;
	}

	public Mapper<FileResource> getMapper() {
		return mapper;
	}

}
