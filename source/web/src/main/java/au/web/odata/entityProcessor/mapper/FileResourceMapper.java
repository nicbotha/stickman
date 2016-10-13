package au.web.odata.entityProcessor.mapper;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.springframework.stereotype.Component;

import au.model.entity.FileResource;
import au.model.entity.FileResourceType;
import au.web.odata.ODataConst;

@Component
public class FileResourceMapper implements Mapper<FileResource> {
	
	public static final String NAME_P = "Name";
	public static final String DESCRIPTION_P = "Description";
	public static final String DOCSTOREID_P = "DocStoreId";
	public static final String DOCSTOREPREVIEWID_P = "DocStorePreviewId";
	public static final String TAGS_P = "Tags";
	public static final String TYPE_P = "Type";

	public FileResource toJPAEntity(Entity from) {
		
		if (from != null) {
			FileResource to = new FileResource();
			copyInto(from, to, HttpMethod.PATCH);
			return to;
		}
		
		return null;
	}

	public Entity toOlingoEntity(FileResource from) {
		
		if (from != null) {
			Entity to = new Entity();
			to.addProperty(createPrimitive(ODataConst.JPA_ENTITY_PKEY, new Integer(from.getId())));
			to.addProperty(createPrimitive(FileResourceMapper.NAME_P, from.getName()));
			to.addProperty(createPrimitive(FileResourceMapper.DESCRIPTION_P, from.getDescription()));
			to.addProperty(createPrimitive(FileResourceMapper.DOCSTOREID_P, from.getDocStoreId()));
			to.addProperty(createPrimitive(FileResourceMapper.DOCSTOREPREVIEWID_P, from.getDocStorePreviewId()));
			to.addProperty(createPrimitive(FileResourceMapper.TAGS_P, from.getTags()));
			to.addProperty(createPrimitive(FileResourceMapper.TYPE_P, from.getType()));
			to.setId(createId(ODataConst.FILERESOURCE_ES_NAME, from.getId()));
			return to;
		}
		
		return null;
	}

	public void copyInto(Entity from, FileResource to, HttpMethod httpMethod) {
		Property nameProperty = from.getProperty(FileResourceMapper.NAME_P);
		Property descriptionProperty = from.getProperty(FileResourceMapper.DESCRIPTION_P);
		Property docStoreIdProperty = from.getProperty(FileResourceMapper.DOCSTOREID_P);
		Property docStorePreviewIdProperty = from.getProperty(FileResourceMapper.DOCSTOREPREVIEWID_P);
		Property tagsProperty = from.getProperty(FileResourceMapper.TAGS_P);
		Property typeProperty = from.getProperty(FileResourceMapper.TYPE_P);

		if(from == null || to == null || httpMethod ==  null) {
			return;
		}
		
		//NAME
		if (nameProperty == null) {
			if (httpMethod.equals(HttpMethod.PATCH)) {
				// Don't do anything as per OData V4 Spec
			} else if (httpMethod.equals(HttpMethod.PUT)) {
				to.setName(null);
			}
		} else {
			to.setName((String) nameProperty.getValue());
		}
		
		//DESCRIPTION
		if (descriptionProperty == null) {
			if (httpMethod.equals(HttpMethod.PATCH)) {
				// Don't do anything as per OData V4 Spec
			} else if (httpMethod.equals(HttpMethod.PUT)) {
				to.setDescription(null);
			}
		} else {
			to.setDescription((String) descriptionProperty.getValue());
		}
		//DOCSTOREID
		if (docStoreIdProperty == null) {
			if (httpMethod.equals(HttpMethod.PATCH)) {
				// Don't do anything as per OData V4 Spec
			} else if (httpMethod.equals(HttpMethod.PUT)) {
				to.setDocStoreId(null);
			}
		} else {
			to.setDocStoreId((String) docStoreIdProperty.getValue());
		}
		//DOCSTOREPREVIEWID
		if (docStorePreviewIdProperty == null) {
			if (httpMethod.equals(HttpMethod.PATCH)) {
				// Don't do anything as per OData V4 Spec
			} else if (httpMethod.equals(HttpMethod.PUT)) {
				to.setDocStorePreviewId(null);
			}
		} else {
			to.setDocStorePreviewId((String) docStorePreviewIdProperty.getValue());
		}
		//TAGS
		if (tagsProperty == null) {
			if (httpMethod.equals(HttpMethod.PATCH)) {
				// Don't do anything as per OData V4 Spec
			} else if (httpMethod.equals(HttpMethod.PUT)) {
				to.setTags(null);
			}
		} else {
			to.setTags((String) tagsProperty.getValue());
		}
		//TYPE
		if (typeProperty == null) {
			if (httpMethod.equals(HttpMethod.PATCH)) {
				// Don't do anything as per OData V4 Spec
			} else if (httpMethod.equals(HttpMethod.PUT)) {
				to.setType(null);
			}
		} else {
			to.setType(FileResourceType.valueOf((String)typeProperty.getValue()));
		}
		
	}

}
