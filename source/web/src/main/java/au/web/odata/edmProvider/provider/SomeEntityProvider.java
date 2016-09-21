package au.web.odata.edmProvider.provider;

import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import au.web.odata.ODataConst;

@Component
public class SomeEntityProvider implements EntityProvider {

	protected static final Logger log = LoggerFactory.getLogger(SomeEntityProvider.class);

	public static final FullQualifiedName ET_FQN = new FullQualifiedName(ODataConst.NAMESPACE, ODataConst.SOMEENTITY_ET_NAME);
	public static final FullQualifiedName ES_FQN = new FullQualifiedName(ODataConst.NAMESPACE, ODataConst.SOMEENTITY_ES_NAME);

	public List<CsdlProperty> getProperties() {
		// create EntityType properties
		CsdlProperty id = new CsdlProperty().setName(ODataConst.JPA_ENTITY_PKEY).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty name = new CsdlProperty().setName("Name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		return Arrays.asList(id, name);
	}
	
	public List<CsdlPropertyRef> getKeys(){
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName(ODataConst.JPA_ENTITY_PKEY);
		return Arrays.asList(propertyRef);
	}

	public String getEntityTypeName() {
		return ODataConst.SOMEENTITY_ET_NAME;
	}

	public String getEntitySetName() {
		return ODataConst.SOMEENTITY_ES_NAME;
	}

	public FullQualifiedName getFullyQualifiedEntityTypeName() {
		return ET_FQN;
	}
}
