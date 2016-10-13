package au.web.odata.edmProvider.provider;

import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.springframework.stereotype.Component;

import au.web.odata.ODataConst;
import au.web.odata.entityProcessor.mapper.FileResourceMapper;

@Component
public class FileResourceProvider implements EntityProvider {

	@Override
	public String getEntitySetName() {
		return ODataConst.FILERESOURCE_ES_NAME;
	}

	@Override
	public String getEntityTypeName() {
		return ODataConst.FILERESOURCE_ET_NAME;
	}

	@Override
	public FullQualifiedName getFullyQualifiedEntityTypeName() {
		return new FullQualifiedName(ODataConst.NAMESPACE, ODataConst.FILERESOURCE_ET_NAME);
	}

	@Override
	public List<CsdlProperty> getProperties() {
		return Arrays.asList(
				property(ODataConst.JPA_ENTITY_PKEY, EdmPrimitiveTypeKind.Int32), property(FileResourceMapper.NAME_P,EdmPrimitiveTypeKind.String),
				property(ODataConst.JPA_ENTITY_PKEY, EdmPrimitiveTypeKind.Int32), property(FileResourceMapper.DESCRIPTION_P,EdmPrimitiveTypeKind.String),
				property(ODataConst.JPA_ENTITY_PKEY, EdmPrimitiveTypeKind.Int32), property(FileResourceMapper.DOCSTOREID_P,EdmPrimitiveTypeKind.String),
				property(ODataConst.JPA_ENTITY_PKEY, EdmPrimitiveTypeKind.Int32), property(FileResourceMapper.DOCSTOREPREVIEWID_P,EdmPrimitiveTypeKind.String),
				property(ODataConst.JPA_ENTITY_PKEY, EdmPrimitiveTypeKind.Int32), property(FileResourceMapper.TAGS_P,EdmPrimitiveTypeKind.String),
				property(ODataConst.JPA_ENTITY_PKEY, EdmPrimitiveTypeKind.Int32), property(FileResourceMapper.TYPE_P,EdmPrimitiveTypeKind.String));
	}

}
