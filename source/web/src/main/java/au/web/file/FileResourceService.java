package au.web.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;

@Component("FileResourceService")
public class FileResourceService implements FileService {

	private static final String CMIS_FOLDER = "cmis:folder";
	private static final String uniqueName = "myUniqueName";
	private static final String secretKey = "mySecret-!5qwWW26&&-";
	private static final String lookupName = "java:comp/env/" + "EcmService";
	protected final Logger log = LoggerFactory.getLogger(FileResourceService.class);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			
			byte[] content = IOUtils.toByteArray(request.getInputStream());			
			String id = create(content);

			FileResourceResponseObject responseObj = new FileResourceResponseObject();
			responseObj.setDocStoreId(id);
			responseObj.setDocStorePreviewId(id);

			String responseStr = objectMapper.writeValueAsString(responseObj);
			response.getWriter().write(responseStr);

		} catch (IOException e) {
			log.error("Error occured during service lookup.", lookupName, e);
		} catch (NamingException e) {
			log.error("Error occured during service lookup.", lookupName, e);
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		String id = request.getParameter("ID");

		Session session;
		try (OutputStream output = response.getOutputStream()) {
			
			session = openCmisSession(uniqueName, secretKey);
			Document document = (Document) session.getObject(session.createObjectId(id));
			InputStream stream = document.getContentStream().getStream();
			response.setHeader("Content-Type", document.getContentStreamMimeType());

			byte[] buffer = new byte[10240];

			for (int length = 0; (length = stream.read(buffer)) > 0;) {
				output.write(buffer, 0, length);
			}
			
		} catch (IOException ioe) {
			log.error("Error occured during service lookup.", lookupName, ioe);
		} catch (NamingException e) {
			log.error("Error occured during service lookup.", lookupName, e);
		}
	}

	@Override
	public String create(byte[] content) throws NamingException {
		try {
			Session openCmisSession = openCmisSession(uniqueName, secretKey);
			Folder folder = getDocumentFolder(openCmisSession, "fileResources");
			Document document = createDocument(openCmisSession, folder, content, UUID.randomUUID().toString());
			return document.getId();
		} catch (NamingException e) {
			log.error("Error occured during service lookup.", lookupName, e);
			throw e;
		}
	}
	
	@Override
	public void delete(String identifier) throws NamingException {
		try {
			Session session = openCmisSession(uniqueName, secretKey);
			session.delete(session.createObjectId(identifier));
		} catch (NamingException e) {
			log.error("Error occured during service lookup.", lookupName, e);
			throw e;
		}		
	}

	protected Document createDocument(Session session, Folder folder, byte[] content, String documentname) {
		// create a new file in the root folder
		Document document = null;
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties.put(PropertyIds.NAME, documentname);
		InputStream stream = new ByteArrayInputStream(content);
		ContentStream contentStream = session.getObjectFactory().createContentStream(documentname, content.length, "application/octet-stream", stream);
		try {
			document = folder.createDocument(properties, contentStream, VersioningState.NONE);
		} catch (CmisNameConstraintViolationException e) {
			// Document exists already, nothing to do
		}

		return document;
	}

	protected Session openCmisSession(String uniqueName, String secretKey) throws NamingException {
		Session openCmisSession = null;
		InitialContext ctx = new InitialContext();

		EcmService ecmSvc = (EcmService) ctx.lookup(lookupName);
		try {
			// connect to my repository
			openCmisSession = ecmSvc.connect(uniqueName, secretKey);
		} catch (CmisObjectNotFoundException e) {
			// repository does not exist, so try to create it
			RepositoryOptions options = new RepositoryOptions();
			options.setUniqueName(uniqueName);
			options.setRepositoryKey(secretKey);
			options.setVisibility(Visibility.PROTECTED);
			ecmSvc.createRepository(options);
			// should be created now, so connect to it
			openCmisSession = ecmSvc.connect(uniqueName, secretKey);
		}

		return openCmisSession;
	}

	protected Folder getDocumentFolder(Session openCmisSession, final String foldername) {
		// create a new folder
		Folder root = openCmisSession.getRootFolder();
		Folder docFolder = null;
		for (CmisObject o : root.getChildren()) {
			Property<Object> pType = o.getProperty(PropertyIds.OBJECT_TYPE_ID);
			Property<Object> pName = o.getProperty(PropertyIds.NAME);

			if (pType.getValueAsString().equals(CMIS_FOLDER)) {
				if (pName.getValueAsString().equals(foldername)) {
					docFolder = (Folder) o;
					break;
				}
			}

		}

		if (docFolder == null) {
			Map<String, String> newFolderProps = new HashMap<String, String>();
			newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, CMIS_FOLDER);
			newFolderProps.put(PropertyIds.NAME, foldername);
			try {
				docFolder = root.createFolder(newFolderProps);
			} catch (CmisNameConstraintViolationException e) {
				// Folder exists already, nothing to do
			}
		}
		return docFolder;
	}

}
