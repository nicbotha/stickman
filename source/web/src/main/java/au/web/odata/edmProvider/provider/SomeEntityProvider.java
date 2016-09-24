package au.web.odata.edmProvider.provider;

import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import au.web.odata.ODataConst;

@Component
public class SomeEntityProvider implements EntityProvider {

	protected static final Logger log = LoggerFactory.getLogger(SomeEntityProvider.class);

	public String getEntityTypeName() {
		return ODataConst.SOMEENTITY_ET_NAME;
	}

	public String getEntitySetName() {
		return ODataConst.SOMEENTITY_ES_NAME;
	}

	public FullQualifiedName getFullyQualifiedEntityTypeName() {
		return new FullQualifiedName(ODataConst.NAMESPACE, ODataConst.SOMEENTITY_ET_NAME);
	}
	
	public List<CsdlProperty> getProperties() {
		return Arrays.asList(property(ODataConst.JPA_ENTITY_PKEY, EdmPrimitiveTypeKind.Int32), property("Name",EdmPrimitiveTypeKind.String));
	}
}
