package au.web.odata.edmProvider.provider;

import java.util.List;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

import au.web.odata.ODataConst;

public interface EntityProvider {
	
	public default CsdlEntityType getEntityType() {
		// create PropertyRef for Key element
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName(ODataConst.JPA_ENTITY_PKEY);

		// configure EntityType
		CsdlEntityType entityType = new CsdlEntityType();
		entityType.setName(getEntityTypeName());
		entityType.setProperties(getProperties());
		entityType.setKey(getKeys());

		return entityType;
	}
	
	public default CsdlEntitySet getEntitySet() {
		return new CsdlEntitySet().setName(getEntitySetName()).setType(getFullyQualifiedEntityTypeName());
	}

	public String getEntitySetName();

	public String getEntityTypeName();

	public FullQualifiedName getFullyQualifiedEntityTypeName();
	
	public List<CsdlProperty> getProperties();
	
	public List<CsdlPropertyRef> getKeys();
}
