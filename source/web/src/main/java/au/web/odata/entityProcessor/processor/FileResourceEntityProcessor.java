package au.web.odata.entityProcessor.processor;

import java.util.List;
import java.util.Locale;

import javax.naming.NamingException;

import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.model.entity.FileResource;
import au.model.repository.FileResourceCRUDRepository;
import au.web.file.FileResourceService;
import au.web.odata.ODataConst;
import au.web.odata.entityProcessor.mapper.FileResourceMapper;
import au.web.odata.entityProcessor.mapper.Mapper;

@Component
public class FileResourceEntityProcessor implements EntityProcessor<FileResourceCRUDRepository, FileResource> {
	protected final Logger log = LoggerFactory.getLogger(FileResourceEntityProcessor.class);

	@Autowired
	protected FileResourceCRUDRepository repository;
	
	@Autowired
	protected FileResourceMapper mapper;
	
	@Autowired
	protected FileResourceService fileresourceService;
	
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
	
	/**
	 * Override - delete JPA entity and document store object
	 * */
	public void delete(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		final UriParameter key = keyParams.get(0);

		if (!(isEntityType(edmEntityType) && isPrimaryKey(key))) {
			throw new ODataApplicationException("Could not delete entitiy", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
		}
		
		FileResource fileResource = getRepository().findOne(key.getText());

		getRepository().delete(key.getText());
		try {
			fileresourceService.delete(fileResource.getDocStoreId());
		} catch (NamingException e) {
			log.error("Error occured while deleting document from FileResourceService.", e);
		}
	}

}
