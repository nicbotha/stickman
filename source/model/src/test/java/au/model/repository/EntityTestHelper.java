package au.model.repository;

import au.model.entity.FileResource;
import au.model.entity.FileResourceType;
import au.model.entity.SomeEntity;

public class EntityTestHelper {

	public static SomeEntity newSomeEntity() {
		SomeEntity linx = new SomeEntity();
		linx.setName("name");
		return linx;
	}
	
	public static FileResource newFileResource() {
		FileResource fileResource = new FileResource();		
		fileResource.setName("name");
		fileResource.setDescription("description");
		fileResource.setDocStoreId("docStoreId");
		fileResource.setDocStorePreviewId("docStorePreviewId");
		fileResource.setTags("tags");
		fileResource.setType(FileResourceType.IMAGE);
		return fileResource;
	}
}
